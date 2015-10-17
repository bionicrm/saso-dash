package io.saso.dash.server.handlers

import com.google.inject.Inject
import com.google.inject.Provider
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler
import io.netty.util.CharsetUtil
import io.netty.util.concurrent.GenericFutureListener
import io.saso.dash.auth.Authenticator
import io.saso.dash.config.Config
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.redis.databases.RedisConnections
import io.saso.dash.services.ServiceManager
import io.saso.dash.util.logThrowable
import java.net.URLDecoder
import java.util.*

public class DashServerHttpHandler
@Inject
constructor(val redisConnections: RedisConnections,
            val authenticator: Authenticator,
            val serviceManagerProvider: Provider<ServiceManager>,
            val handlerFactory: ServerHandlerFactory,
            val config: Config) : ServerHttpHandler()
{
    private val url = config.get("server.url", "ws://127.0.0.1")

    override fun messageReceived(ctx: ChannelHandlerContext,
                                 msg: FullHttpRequest)
    {
        val ch = ctx.channel()

        // check request validity
        if (msg.decoderResult().isFailure) {
            respond(ctx, HttpResponseStatus.BAD_REQUEST)
            return
        }

        val liveToken = authenticate(msg)

        // if authentication failure, send 403
        if (! liveToken.isPresent) {
            respond(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }

        // if too many concurrent requests, send 429
        if (! (redisConnections addIfAllowed liveToken.get().userId)) {
            respond(ctx, HttpResponseStatus.TOO_MANY_REQUESTS)
            return
        }

        val wsFactory = WebSocketServerHandshakerFactory(url, null, false)
        val handshaker = wsFactory.newHandshaker(msg)

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ch)
            return
        }

        val wsHandler = handlerFactory createServerWSHandler handshaker
        val serviceManager = serviceManagerProvider.get()
        val pipeline = ch.pipeline()

        // set up ws pipeline
        pipeline remove "httphandler"
        pipeline addLast WebSocketServerCompressionHandler()
        pipeline addLast wsHandler

        // handshake; callback: start ServiceManager
        handshaker.handshake(ch, msg) addListener GenericFutureListener {
            serviceManager.start(ctx, liveToken.get())
        }

        // on channel close: stop ServiceManager
        ch.closeFuture() addListener GenericFutureListener {
            serviceManager.stop(ctx, liveToken.get())
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable)
    {
        logThrowable(this, e)
        ctx.close()
    }

    private fun authenticate(req: FullHttpRequest): Optional<LiveToken>
    {
        val token = getCookieValue(req.headers(), "live_token")

        if (token.isPresent) {
            return authenticator findLiveToken token.get()
        }

        return Optional.empty()
    }

    private fun respond(ctx: ChannelHandlerContext, status: HttpResponseStatus)
    {
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer(status.toString(), CharsetUtil.UTF_8))

        (ctx writeAndFlush response) addListener ChannelFutureListener.CLOSE
    }

    private fun getCookieValue(headers: HttpHeaders,
                               name: String): Optional<String>
    {
        headers.getAllAndConvert(HttpHeaderNames.COOKIE).forEach {
            val cookie = ClientCookieDecoder.decode(it)

            if (cookie.name() == name) {
                return Optional.of(URLDecoder.decode(cookie.value(), "UTF-8"))
            }
        }

        return Optional.empty()
    }
}
