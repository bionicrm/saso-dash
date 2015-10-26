package io.saso.dash.server.handlers.ws

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker

class DashWSHandler extends SimpleChannelInboundHandler<WebSocketFrame>
{
    private final WebSocketServerHandshaker handshaker

    @Inject
    DashWSHandler(@Assisted WebSocketServerHandshaker handshaker)
    {
        this.handshaker = handshaker
    }

    @Override
    void messageReceived(ChannelHandlerContext ctx, WebSocketFrame msg)
    {
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), msg.retain())
        }
        else if (msg instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(
                    new PongWebSocketFrame(msg.content().retain()))
        }
    }
}
