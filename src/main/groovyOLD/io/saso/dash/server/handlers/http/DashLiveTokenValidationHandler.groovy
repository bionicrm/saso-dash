package io.saso.dash.server.handlers.http

import com.google.inject.Singleton
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.server.events.UpgradeRequestEvent
import io.saso.dash.util.HandlerUtil

import java.sql.Timestamp
import java.time.Instant

@Singleton @ChannelHandler.Sharable
class DashLiveTokenValidationHandler extends ChannelHandlerAdapter
{
    @Override
    void userEventTriggered(ChannelHandlerContext ctx, event)
    {
        final propagate = { ctx.fireUserEventTriggered(event) }

        final forbid = {
            HandlerUtil.sendResponseAndClose(ctx, HttpResponseStatus.FORBIDDEN)
        }

        if (event instanceof UpgradeRequestEvent) {
            final liveTokenExpiry = event.entityProvider.liveToken.expiresAt
            final now = Timestamp.from Instant.now()

            // if the live token's expiry timestamp is in the future...
            if (liveTokenExpiry.after(now)) {
                propagate()
            }
            else {
                forbid()
            }
        }
        else {
            propagate()
        }
    }
}
