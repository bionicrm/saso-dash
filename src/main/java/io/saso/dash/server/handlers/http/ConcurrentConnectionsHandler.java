package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.util.ChannelHandlerAttr;
import io.saso.dash.util.ChannelHandlerUtil;
import io.saso.dash.util.ThreadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class ConcurrentConnectionsHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();

    private final ConcurrentConnections concurrentConnections;

    @Inject
    public ConcurrentConnectionsHandler(
            ConcurrentConnections concurrentConnections)
    {
        this.concurrentConnections = concurrentConnections;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        req.retain();

        DBLiveToken liveToken = ctx.attr(ChannelHandlerAttr.LIVE_TOKEN).get();
        final int userId = liveToken.getUserId();

        ThreadUtil.CACHED_THREAD_POOL.execute(() -> {
            logger.debug("Incrementing concurrent connection for user {}",
                    userId);

            boolean allowed = concurrentConnections.incrementIfAllowed(userId);

            if (allowed) {
                ctx.channel().closeFuture().addListener(future -> {
                    logger.debug("Decrementing concurrent connection for " +
                            "user {}", userId);
                    concurrentConnections.decrement(userId);
                });
                ctx.executor().execute(() -> ctx.fireChannelRead(req));
            }
            else {
                ChannelHandlerUtil.respond(ctx,
                        HttpResponseStatus.TOO_MANY_REQUESTS,
                        HttpHeaderUtil.isKeepAlive(req));
                req.release();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
