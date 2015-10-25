package io.saso.dash.server.handlers.http
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.util.HandlerUtil

class DashUserLimitsHandler extends ChannelHandlerAdapter
{
    @Override
    void userEventTriggered(ChannelHandlerContext ctx, eventObj)
    {
        // if the event object is a DBEntityProvider that was just created...
        if (eventObj instanceof DBEntityProvider) {
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
