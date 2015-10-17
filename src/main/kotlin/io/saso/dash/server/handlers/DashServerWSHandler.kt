package io.saso.dash.server.handlers

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.*
import io.saso.dash.util.logThrowable

public class DashServerWSHandler
@Inject
constructor(
        @Assisted val handshaker: WebSocketServerHandshaker): ServerWSHandler()
{
    override fun messageReceived(ctx: ChannelHandlerContext,
                                 msg: WebSocketFrame)
    {
        val ch = ctx.channel()

        if (msg is CloseWebSocketFrame) {
            handshaker.close(ch, msg.retain())
        }
        else if (msg is PingWebSocketFrame) {
            ch.write(PongWebSocketFrame(msg.content().retain()))
        }
        else if (msg is TextWebSocketFrame) {
            val text = msg.retain().text()

            // TODO: handle text frame?
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable)
    {
        logThrowable(this, e)
        ctx.channel().close()
    }
}
