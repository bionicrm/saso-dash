package io.saso.dash.server.handlers.ws;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.saso.dash.util.ContextAttr;

@ChannelHandler.Sharable
public class CloseFrameHandler
        extends SimpleChannelInboundHandler<CloseWebSocketFrame>
{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   CloseWebSocketFrame frame)
    {
        ctx.attr(ContextAttr.WS_HANDSHAKER).get().close(ctx.channel(),
                frame.retain());
    }
}
