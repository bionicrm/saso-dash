package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
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
import io.saso.dash.auth.AuthenticatorOLD;
import io.saso.dash.config.Config;
import io.saso.dash.database.entities.LiveToken;
import io.saso.dash.redis.tables.RedisConnections;
import io.saso.dash.services.ServiceManager;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

public class DashServerHttpHandler extends ServerHttpHandler
{
    private static final int MAX_CONCURRENT_CONNECTIONS_PER_USER = 3;

    private final RedisConnections redisConnections;
    private final Authenticator authenticator;
    private final Provider<ServerWSHandler> wsHandlerProvider;
    private final Provider<ServiceManager> serviceManagerProvider;
    private final String url;

    @Inject
    public DashServerHttpHandler(
            RedisConnections redisConnections,
            Authenticator authenticator,
            Provider<ServerWSHandler> wsHandlerProvider,
            Provider<ServiceManager> serviceManagerProvider,
            Config config)
    {
        this.redisConnections = redisConnections;
        this.authenticator = authenticator;
        this.wsHandlerProvider = wsHandlerProvider;
        this.serviceManagerProvider = serviceManagerProvider;
        url = config.get("server.url", "ws://127.0.0.1");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest msg) throws Exception
    {
        LogManager.getLogger().entry(ctx, msg.method());

        // check request validity
        if (msg.decoderResult().isFailure()) {
            respond(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        final LiveToken liveToken = authenticate(msg);

        // if authentication failure, send 403
        if (liveToken == null) {
            respond(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        // if too many concurrent requests, send 429
        if (! redisConnections.addIfAllowed(liveToken.getUserId())) {
            respond(ctx, HttpResponseStatus.TOO_MANY_REQUESTS);
            return;
        }

        final WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(url, null, false);

        final WebSocketServerHandshaker handshaker =
                wsFactory.newHandshaker(msg);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
            return;
        }

        final ServerWSHandler wsHandler = wsHandlerProvider.get();
        final ServiceManager serviceManager = serviceManagerProvider.get();
        final ChannelPipeline pipeline = ctx.channel().pipeline();

        // inject wsHandler
        wsHandler.setHandshaker(handshaker);

        // set up ws pipeline
        pipeline.remove("httphandler");
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(wsHandler);

        // handshake; callback: start ServiceManager
        handshaker.handshake(ctx.channel(), msg).addListener(future ->
                serviceManager.start(ctx, liveToken));

        // on channel close: stop ServiceManager
        ctx.channel().closeFuture().addListener(future ->
                serviceManager.stop(ctx, liveToken));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LoggingUtil.logThrowable(cause, getClass());
        ctx.close();
    }

    /**
     * Authenticates an incoming WebSocket connection with its
     * {@code live_token} cookie value. Returns an empty Optional if the token
     * is invalid (expired) or not found.
     *
     * @param req the request to authenticate
     *
     * @return a LiveToken
     *
     * @see AuthenticatorOLD#findLiveToken(String)
     * @see LiveToken#getToken()
     *
     * @throws Exception
     */
    private LiveToken authenticate(FullHttpRequest req)
            throws Exception
    {
        final Optional<String> token =
                getCookieValue(req.headers(), "live_token");

        if (token.isPresent()) {
            return authenticator.findLiveToken(token.get());
        }

        return null;
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
