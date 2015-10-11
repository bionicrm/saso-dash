package io.saso.dash.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;

public class DashServerWSHandler extends ServerWSHandler
{
    private WebSocketServerHandshaker handshaker;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, WebSocketFrame msg)
            throws Exception
    {
        LogManager.getLogger().entry(ctx, msg);

        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
        }
        else if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        else if (msg instanceof TextWebSocketFrame) {
            final String text = ((TextWebSocketFrame) msg.retain()).text();

            // TODO: handle messages?
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LoggingUtil.logThrowable(cause, getClass());
        ctx.close();
    }

    @Override
    public void setHandshaker(WebSocketServerHandshaker handshaker)
    {
        this.handshaker = handshaker;
    }
}
