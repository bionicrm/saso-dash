package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.util.CharsetUtil;
import io.saso.dash.auth.Authenticator;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.client.Client;
import io.saso.dash.client.ClientFactory;
import io.saso.dash.config.Config;
import io.saso.dash.util.LoggingUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

public class DashServerHttpHandler extends ServerHttpHandler
{
    private final Authenticator authenticator;
    private final ClientFactory clientFactory;
    private final ServerFactory serverFactory;
    private final String url;

    @Inject
    public DashServerHttpHandler(Authenticator authenticator,
                                 ClientFactory clientFactory,
                                 ServerFactory serverFactory, Config config)
    {
        this.authenticator = authenticator;
        this.clientFactory = clientFactory;
        this.serverFactory = serverFactory;
        url = config.get("server.url", "ws://127.0.0.1");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest msg) throws Exception
    {
        // check request validity
        if (msg.decoderResult().isFailure()) {
            respond(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        final Optional<Client> client = authenticate(ctx, msg);

        // if authentication failure, send 403
        if (! client.isPresent()) {
            respond(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        final WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(url, null, false);

        final Optional<WebSocketServerHandshaker> handshaker =
                Optional.ofNullable(wsFactory.newHandshaker(msg));

        handshaker.ifPresent(h -> {
            // register onClose for client
            ctx.channel().closeFuture().addListener(future ->
                    client.get().onClose(ctx));

            final ChannelPipeline pipeline = ctx.channel().pipeline();

            // set up ws pipeline
            pipeline.remove("httphandler");
            pipeline.addLast(new WebSocketServerCompressionHandler());
            pipeline.addLast(serverFactory.createWSHandler(h, client.get()));

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
        LoggingUtil.logThrowable(cause, getClass());
        ctx.close();
    }

    /**
     * Authenticates an incoming WebSocket connection with its
     * {@code live_token} cookie value. If successful, the WebSocket handshake
     * will be initiated. Otherwise, the request is 403'd (forbidden).
     *
     * @param ctx the context to send responses through
     * @param req the request to authenticate
     *
     * @return an Optional of a created client
     *
     * @see LiveToken#getToken()
     *
     * @throws Exception
     */
    private Optional<Client> authenticate(ChannelHandlerContext ctx,
                                          FullHttpRequest req)
            throws Exception
    {
        final Optional<String> liveTokenHeader =
                getCookieValue(req.headers(), "live_token");

        if (liveTokenHeader.isPresent()) {
            final String s = liveTokenHeader.get();

            final Optional<LiveToken> liveTokenEntity =
                    authenticator.findLiveToken(s);

            if (liveTokenEntity.isPresent()) {
                final LiveToken e = liveTokenEntity.get();

                final WebSocketServerHandshakerFactory wsFactory =
                        new WebSocketServerHandshakerFactory(url, null, true);

                final WebSocketServerHandshaker handshaker =
                        wsFactory.newHandshaker(req);

                if (handshaker == null) {
                    WebSocketServerHandshakerFactory
                            .sendUnsupportedVersionResponse(ctx.channel());
                }
                else {
                    handshaker.handshake(ctx.channel(), req);
                }

                return Optional.of(clientFactory.createClient(e));
            }
        }

        return Optional.empty();
    }

    /**
     * Sends an HTTP response to the channel.
     *
     * @param ctx the context to send the response through
     * @param status the status to send
     */
    private void respond(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        final FullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                        Unpooled.copiedBuffer(status.toString(),
                                CharsetUtil.UTF_8));

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Gets the real value of a cookie (rather than URL encoded). Returns an
     * empty Optional if the cookie is not present in the headers.
     *
     * @param headers the headers of the request
     * @param name the name of the cookie to get the value of
     *
     * @return an Optional of the cookie's value
     *
     * @throws UnsupportedEncodingException
     */
    private Optional<String> getCookieValue(HttpHeaders headers, String name)
            throws UnsupportedEncodingException
    {
        for (String s : headers.getAllAndConvert(HttpHeaderNames.COOKIE)) {
            // decode header string
            final Cookie cookie = ClientCookieDecoder.decode(s);

            if (cookie.name().equals(name)) {
                // URL decode cookie value
                return Optional.of(URLDecoder.decode(cookie.value(), "UTF-8"));
            }
        }

        return Optional.empty();
    }
}
