package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServerWSHandler extends ServerWSHandler
{
    private static final Logger logger = LogManager.getLogger();

    private final WebSocketServerHandshaker handshaker;

    @Inject
    public DashServerWSHandler(@Assisted WebSocketServerHandshaker handshaker)
    {
        this.handshaker = handshaker;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx,
                                   WebSocketFrame msg)
    {
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
            return;
        }

        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            return;
        }

        if (msg instanceof TextWebSocketFrame) {
            final String text = ((TextWebSocketFrame) msg).text();

            logger.trace("{} => {}", ctx.channel().remoteAddress(), text);

            // TODO: off to handlers...

            ctx.channel().writeAndFlush(new TextWebSocketFrame("test"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
