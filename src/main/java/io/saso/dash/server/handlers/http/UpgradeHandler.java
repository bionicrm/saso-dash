package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class UpgradeHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();

    private final String serverUrl;
    private final ChannelHandler[] handlers;

    @Inject
    public UpgradeHandler(Config config,
            @Named("server ws handlers") ChannelHandler[] handlers)
    {
        this.handlers = handlers;
        serverUrl = config.<String>get("server.url").orElse("ws://127.0.0.1");
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
            handshaker.handshake(ctx.channel(), req).addListener(future -> {
                ChannelPipeline p = ctx.channel().pipeline();

                // remove pipeline handlers
                while (p.last() != null) {
                    p.removeLast();
                }

                // add WS channel handlers
                p.addLast(new WebSocketServerCompressionHandler());
                p.addLast(handlers);

                ctx.fireChannelRead(req);
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
