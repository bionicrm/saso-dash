package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.saso.dash.auth.Authenticator;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.client.Client;
import io.saso.dash.client.ClientFactory;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Deprecated
public class DashServerHandlerOLD extends ServerHandlerOLD
{
    private static final Logger logger = LogManager.getLogger();

    private final String url;
    private final Authenticator authenticator;
    private final ClientFactory clientFactory;

    private Client client;
    private WebSocketServerHandshaker handshaker;

    @Inject
    public DashServerHandlerOLD(Authenticator authenticator, Config config,
                                ClientFactory clientFactory)
    {
        this.authenticator = authenticator;
        this.clientFactory = clientFactory;

        url = config.getString("server.url");

        logger.trace("DashServerHandler::new");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg)
    {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        else if (msg instanceof WebSocketFrame)
        {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.trace("remote={} DashServerHandler#handlerRemoved",
                ctx.channel().remoteAddress());
        client.onClose();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        logger.trace("remote={} DashServerHandler#handleHttpRequest",
                ctx.channel().remoteAddress());

        // if bad request
        if (! req.decoderResult().isSuccess()) {
            sendStatusResponse(ctx, req, HttpResponseStatus.BAD_REQUEST);
        }
        // if not GET
        else if (req.method() != HttpMethod.GET) {
            sendStatusResponse(ctx, req, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }
        // handshake...
        else {
            final Optional<String> liveToken =
                    getCookieValue(req.headers(), "live_token");

            liveToken.ifPresent(s -> {
                logger.debug("remote={} live_token=\"{}\"",
                        ctx.channel().remoteAddress(), s);

                final Optional<LiveToken> liveTokenEntity =
                        authenticator.findValidLiveToken(s);

                liveTokenEntity.ifPresent(e -> {
                    final WebSocketServerHandshakerFactory wsFactory =
                            new WebSocketServerHandshakerFactory(
                                    url, null, true);

                    handshaker = wsFactory.newHandshaker(req);

                    if (handshaker == null) {
                        WebSocketServerHandshakerFactory
                                .sendUnsupportedVersionResponse(ctx.channel());
                    }
                    else {
                        handshaker.handshake(ctx.channel(), req);

                        client = clientFactory.createClient(ctx, e);
                    }
                });

                if (! liveTokenEntity.isPresent()) {
                    logger.debug(
                            "remote={} authenticator.isTokenValid -> false",
                            ctx.channel().remoteAddress());
                }
            });

            if (! liveToken.isPresent()) {
                sendStatusResponse(ctx, req, HttpResponseStatus.FORBIDDEN);
            }
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest req, FullHttpResponse res)
    {
        final boolean notOk = res.status().code() != 200;

        if (notOk) {
            final ByteBuf buf = Unpooled.copiedBuffer(
                    res.status().toString(), CharsetUtil.UTF_8);

            res.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
        }

        final ChannelFuture f = ctx.channel().writeAndFlush(res);

        if (! HttpHeaderUtil.isKeepAlive(req) || notOk) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame)
    {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
        }
        else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(
                    frame.content().retain()));
        }
        else if (! (frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(frame.getClass().getName()
                    + " frame types not supported");
        }
        else {
            final String received = ((TextWebSocketFrame) frame).text();

            logger.trace("remote={} frame=\"{}\"",
                    ctx.channel().remoteAddress(), received);
            client.onFrame(received);
            /*ctx.channel().writeAndFlush(new TextWebSocketFrame());*/
        }
    }

    private void sendStatusResponse(ChannelHandlerContext ctx,
                                    FullHttpRequest req,
                                    HttpResponseStatus status)
    {
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status));
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
