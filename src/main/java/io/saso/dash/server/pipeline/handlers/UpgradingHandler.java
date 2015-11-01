package io.saso.dash.server.pipeline.handlers;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.server.pipeline.upgrading.ConcurrentConnectionsHandler;
import io.saso.dash.server.pipeline.upgrading.UpgradeHandler;
import io.saso.dash.util.ChannelHandlerUtil;
import io.saso.dash.util.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.Executors;

@ChannelHandler.Sharable
public class UpgradingHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();
    private static final String sql = Resources.get("/sql/find_live_token.sql");

    private static final
    ListeningExecutorService service = MoreExecutors.listeningDecorator(
            Executors.newCachedThreadPool());

    private final DBFetcher entityFetcher;
    private final CookieFinder cookieFinder;
    private final UpgradeHandler[] upgradeHandlers;

    @Inject
    public UpgradingHandler(
            DBFetcher entityFetcher, CookieFinder cookieFinder,
            ConcurrentConnectionsHandler concurrentConnectionsHandler)
    {
        this.entityFetcher = entityFetcher;
        this.cookieFinder = cookieFinder;
        upgradeHandlers = new UpgradeHandler[] { concurrentConnectionsHandler };
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        Futures.addCallback(service.submit(() -> {
            if (upgradeHandlers.length > 0) {
                Optional<String> token =
                        cookieFinder.find("live_token", req.headers());

                if (token.isPresent()) {
                    for (int i = 0; i < upgradeHandlers.length; i++) {
                        UpgradeHandler handler = upgradeHandlers[i];

                        if (i == upgradeHandlers.length - 1) {
                            handler.setNext(new NullUpgradeHandler());
                        }
                        else {
                            handler.setNext(upgradeHandlers[i + 1]);
                        }
                    }

                    Optional<DBLiveToken> liveToken = entityFetcher.fetch(
                            DBLiveToken.class, sql, token.get());

                    if (liveToken.isPresent()) {
                        upgradeHandlers[0].upgrade(ctx, req, liveToken.get());
                        return true;
                    }
                }
            }

            return false;
        }), new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result)
            {
                if (result) {
                    ctx.fireChannelRead(req.retain());
                }
                else {
                    ChannelHandlerUtil.respond(
                            ctx, HttpResponseStatus.FORBIDDEN,
                            HttpHeaderUtil.isKeepAlive(req));
                }
            }

            @Override
            public void onFailure(@Nonnull Throwable t)
            {
                throw new RuntimeException(t);
            }
        }, ctx.executor());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private class NullUpgradeHandler implements UpgradeHandler
    {
        @Override
        public void upgrade(ChannelHandlerContext ctx, FullHttpRequest req,
                            DBLiveToken liveToken) { }

        @Override
        public void setNext(UpgradeHandler next) { }
    }
}
