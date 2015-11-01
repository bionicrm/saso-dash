package io.saso.dash.server.handlers.ws;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;

@ChannelHandler.Sharable
public class PingFrameHandler
        extends SimpleChannelInboundHandler<PingWebSocketFrame>
{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   PingWebSocketFrame frame)
    {
        ctx.channel().writeAndFlush(new PongWebSocketFrame(
                frame.content().retain()));
        ctx.fireChannelRead(frame.retain());
    }
}
