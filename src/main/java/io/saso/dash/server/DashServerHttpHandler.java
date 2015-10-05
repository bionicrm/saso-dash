package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.util.CharsetUtil;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

public class DashServerHttpHandler extends ServerHttpHandler
{
    private static final Logger logger = LogManager.getLogger();

    private final ServerFactory serverFactory;
    private final String url;

    @Inject
    public DashServerHttpHandler(ServerFactory serverFactory, Config config)
    {
        this.serverFactory = serverFactory;
        url = config.getString("server.url", "ws://127.0.0.1");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest msg)
    {
        if (msg.decoderResult().isFailure()) {
            respond(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        // TODO: validate live_token

        final WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(url, null, false);

        final Optional<WebSocketServerHandshaker> handshaker =
                Optional.ofNullable(wsFactory.newHandshaker(msg));

        handshaker.ifPresent(h -> {
            final ChannelPipeline pipeline = ctx.channel().pipeline();

            // set up ws pipeline
            pipeline.remove("httpaggregator");
            pipeline.remove("httphandler");
            pipeline.addLast(new WebSocketServerCompressionHandler());
            pipeline.addLast(serverFactory.createWSHandler(h));

            h.handshake(ctx.channel(), msg);
        });

        if (! handshaker.isPresent()) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private void respond(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        final FullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                        Unpooled.copiedBuffer(status.toString(),
                                CharsetUtil.UTF_8));

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Optional<String> getCookieValue(HttpHeaders headers, String name) {
        for (String s : headers.getAllAndConvert(HttpHeaderNames.COOKIE)) {
            // decode header string
            final Cookie cookie = ClientCookieDecoder.decode(s);

            if (cookie.name().equals(name)) {
                try {
                    // URL decode cookie value
                    return Optional.of(
                            URLDecoder.decode(cookie.value(), "UTF-8"));
                }
                catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return Optional.empty();
    }
}
