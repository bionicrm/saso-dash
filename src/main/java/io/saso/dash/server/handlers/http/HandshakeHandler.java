package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.saso.dash.util.ContextAttr;
import me.mazeika.uconfig.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class HandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();

    private final String serverUrl;

    @Inject
    public HandshakeHandler(Config config)
    {
        serverUrl = config.getOrDefault("server.url", "ws://127.0.0.1");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        req.retain();

        WebSocketServerHandshakerFactory handshakerFactory =
                new WebSocketServerHandshakerFactory(serverUrl, null, false);
        WebSocketServerHandshaker handshaker =
                handshakerFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
            req.release();
        }
        else {
            ctx.attr(ContextAttr.WS_HANDSHAKER).set(handshaker);
            handshaker.handshake(ctx.channel(), req).addListener(future ->
                    ctx.fireChannelRead(req));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
