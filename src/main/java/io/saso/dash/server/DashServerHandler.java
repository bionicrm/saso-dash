package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

public class DashServerHandler extends ServerHandler
{
    private static final Logger logger = LogManager.getLogger();

    private final String url;

    private Optional<WebSocketServerHandshaker> handshaker = Optional.empty();

    @Inject
    public DashServerHandler(Config config)
    {
        url = config.getString("server.url", "ws://127.0.0.1");

        logger.trace("DashServerHandler::new");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg)
    {
        if (msg instanceof FullHttpRequest) {
            onFullHttpRequest(ctx, (FullHttpRequest) msg);
        }
        else if (msg instanceof WebSocketFrame) {
            onWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.trace("DashServerHandler#handlerRemoved: remote={}",
                ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private void onFullHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest msg)
    {
        if (msg.decoderResult().isFailure()) {
            respond(ctx, HttpResponseStatus.BAD_REQUEST);
        }
        else {
            // TODO: validate live_token

            final WebSocketServerHandshakerFactory wsFactory =
                    new WebSocketServerHandshakerFactory(url, null, false);

            handshaker = Optional.ofNullable(wsFactory.newHandshaker(msg));

            handshaker.ifPresent(handshaker ->
                    handshaker.handshake(ctx.channel(), msg));

            if (! handshaker.isPresent()) {
                WebSocketServerHandshakerFactory
                        .sendUnsupportedVersionResponse(ctx.channel());
            }
        }
    }

    private void onWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg)
    {
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.get().close(ctx.channel(),
                    (CloseWebSocketFrame) msg.retain());
        }
        else if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        else if (msg instanceof TextWebSocketFrame) {
            final String text = ((TextWebSocketFrame) msg).text();

            logger.trace(
                    "DashServerHandler#onWebSocketFrame: remote={} text=\"{}\"",
                    ctx.channel().remoteAddress(), text);

            // TODO: off to handlers...
            ctx.channel().writeAndFlush(new TextWebSocketFrame("hello world"));
        }
    }

    private void respond(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(status.toString(), CharsetUtil.UTF_8));

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
