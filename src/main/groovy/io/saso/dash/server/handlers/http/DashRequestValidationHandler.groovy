package io.saso.dash.server.handlers.http
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.util.HandlerUtil

/**
 * Represents a Netty pipeline handler that validates incoming requests,
 * forwarding the request to the next handler if validation is successful.
 */
class DashRequestValidationHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {
        if (msg.decoderResult().isSuccess()) {
            // propagate
            ctx.fireChannelRead(msg)
        }
        else {
            HandlerUtil.sendResponseAndClose(ctx,
                    HttpResponseStatus.BAD_REQUEST)
        }
    }
}
