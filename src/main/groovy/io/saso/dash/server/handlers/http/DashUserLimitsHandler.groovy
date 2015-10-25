package io.saso.dash.server.handlers.http

import com.google.inject.Inject
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.redis.databases.ConcurrentConnections
import io.saso.dash.util.HandlerUtil

@ChannelHandler.Sharable
class DashUserLimitsHandler extends ChannelHandlerAdapter
{
    private final ConcurrentConnections concurrentConnections

    @Inject
    DashUserLimitsHandler(ConcurrentConnections concurrentConnections)
    {
        this.concurrentConnections = concurrentConnections
    }

    @Override
    void userEventTriggered(ChannelHandlerContext ctx, eventObj)
    {
        if (eventObj instanceof DBEntityProvider
                && ! concurrentConnections.addConnection(
                eventObj.liveToken.userId)) {
            HandlerUtil.sendResponseAndClose(ctx,
                    HttpResponseStatus.TOO_MANY_REQUESTS)
        }
        else {
            // propagate
            ctx.fireUserEventTriggered(eventObj)
        }
    }
}
