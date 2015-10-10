package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import io.saso.dash.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServerWSHandler extends ServerWSHandler
{
    private static final Logger logger = LogManager.getLogger();

    private final WebSocketServerHandshaker handshaker;
    private final Client client;

    private boolean closeFutureSet;

    @Inject
    public DashServerWSHandler(@Assisted WebSocketServerHandshaker handshaker,
                               @Assisted Client client)
    {
        this.handshaker = handshaker;
        this.client = client;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx,
                                   WebSocketFrame msg)
    {
        if (! closeFutureSet) {
            ctx.channel().closeFuture().addListener(future ->
                    client.onClose(ctx));
            closeFutureSet = true;
        }

        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
        }
        else if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        else if (msg instanceof TextWebSocketFrame) {
            final String text = ((TextWebSocketFrame) msg).text();

            client.onFrame(ctx, text);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
