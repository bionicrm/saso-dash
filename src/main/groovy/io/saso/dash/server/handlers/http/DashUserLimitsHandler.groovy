package io.saso.dash.server.handlers.http
import com.google.inject.Inject
import com.google.inject.Singleton
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.redis.databases.ConcurrentConnections
import io.saso.dash.server.events.UpgradeRequestEvent
import io.saso.dash.util.HandlerUtil

@Singleton @ChannelHandler.Sharable
class DashUserLimitsHandler extends ChannelHandlerAdapter
{
    private final ConcurrentConnections concurrentConnections

    @Inject
    DashUserLimitsHandler(ConcurrentConnections concurrentConnections)
    {
        this.concurrentConnections = concurrentConnections
    }

    @Override
    void userEventTriggered(ChannelHandlerContext ctx, event)
    {
        final propagate = { ctx.fireUserEventTriggered(event) }

        final tooManyRequests = { HandlerUtil.sendResponseAndClose(ctx,
                HttpResponseStatus.TOO_MANY_REQUESTS) }

        if (event instanceof UpgradeRequestEvent) {
            final userId = event.entityProvider.liveToken.userId

            if (concurrentConnections.addConnection(userId)) {
                propagate()
            }
            else {
                tooManyRequests()
            }
        }
        else {
            propagate()
        }
    }
}
