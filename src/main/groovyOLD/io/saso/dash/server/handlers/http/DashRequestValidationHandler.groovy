package io.saso.dash.server.handlers.http
import com.google.inject.Singleton
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.saso.dash.util.HandlerUtil
/**
 * Represents a Netty pipeline handler that validates incoming requests,
 * forwarding the request to the next handler if validation is successful.
 */
@Singleton @ChannelHandler.Sharable
class DashRequestValidationHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {
        msg.retain()

        final propagate = { ctx.fireChannelRead(msg) }

        final badRequest = {
            HandlerUtil.sendResponseAndClose(ctx,
                    HttpResponseStatus.BAD_REQUEST)
        }

        if (msg.decoderResult().isSuccess()) {
            propagate()
        }
        else {
            badRequest()
        }
    }
}
