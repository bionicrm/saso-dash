package io.saso.dash.server.pipeline.upgrading;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.util.ChannelHandlerUtil;

public class ConcurrentConnectionsHandler implements UpgradeHandler
{
    private final ConcurrentConnections concurrentConnections;

    private UpgradeHandler next;

    @Inject
    public ConcurrentConnectionsHandler(
            ConcurrentConnections concurrentConnections)
    {
        this.concurrentConnections = concurrentConnections;
    }

    @Override
    public void upgrade(ChannelHandlerContext ctx, FullHttpRequest req,
                        DBLiveToken liveToken)
    {
        boolean allowed =
                concurrentConnections.incrementIfAllowed(liveToken.getUserId());

        if (allowed) {
            next.upgrade(ctx, req, liveToken);
        }
        else {
            ChannelHandlerUtil.respond(
                    ctx, HttpResponseStatus.TOO_MANY_REQUESTS,
                    HttpHeaderUtil.isKeepAlive(req));
        }
    }

    @Override
    public void setNext(UpgradeHandler next)
    {
        this.next = next;
    }
}
