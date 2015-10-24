package io.saso.dash.server.handlers.http
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.util.HandlerUtil

class DashUserLimitsHandler extends ChannelHandlerAdapter
{
    @Override
    void userEventTriggered(ChannelHandlerContext ctx, Object eventObj)
    {
        // if the event object is a DBLiveToken that was just fetched...
        if (eventObj instanceof DBLiveToken) {
            // TODO: check concurrent users

            // forbid the request
            HandlerUtil.sendResponseAndClose(ctx, HttpResponseStatus.FORBIDDEN)
        }
        else {
            // propagate the event
            ctx.fireUserEventTriggered(eventObj)
        }
    }
}
