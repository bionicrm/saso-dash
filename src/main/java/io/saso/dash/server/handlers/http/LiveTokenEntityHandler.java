package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.server.Authentication;
import io.saso.dash.util.ChannelHandlerAttr;
import io.saso.dash.util.ChannelHandlerUtil;
import io.saso.dash.util.ThreadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@ChannelHandler.Sharable
public class LiveTokenEntityHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();

    private final Authentication authentication;

    @Inject
    public LiveTokenEntityHandler(Authentication authentication)
    {
        this.authentication = authentication;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        req.retain();

        String token = ctx.attr(ChannelHandlerAttr.TOKEN_COOKIE_VALUE).get();

        ThreadUtil.CACHED_THREAD_POOL.execute(() -> {
            Optional<DBLiveToken> liveTokenOptional =
                    authentication.authenticate(token);

            if (liveTokenOptional.isPresent()) {
                ctx.attr(ChannelHandlerAttr.LIVE_TOKEN).set(
                        liveTokenOptional.get());
                ctx.executor().execute(() -> ctx.fireChannelRead(req));
            }
            else {
                ChannelHandlerUtil.respond(ctx, HttpResponseStatus.FORBIDDEN,
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
