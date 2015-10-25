package io.saso.dash.server.handlers.http

import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.util.HandlerUtil

import java.sql.Timestamp
import java.time.Instant

class DashLiveTokenValidationHandler extends ChannelHandlerAdapter
{
    @Override
    void userEventTriggered(ChannelHandlerContext ctx, eventObj)
    {
        // if the event object is a DBEntityProvider that was just created...
        if (eventObj instanceof DBEntityProvider
                // if the expiry date of the live token is on or before the
                // current date...
                && ! eventObj.liveToken.expiresAt.after(Timestamp.from(
                Instant.now()))) {
            // forbid the request
            HandlerUtil.sendResponseAndClose(ctx, HttpResponseStatus.FORBIDDEN)
        }
        else {
            // propagate the event
            ctx.fireUserEventTriggered(eventObj)
        }
    }
}
