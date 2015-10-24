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
import io.saso.dash.redis.databases.RedisConnections
import io.saso.dash.services.ServiceManager
import io.saso.dash.util.ifPresent
import io.saso.dash.util.logThrowable
import java.net.URLDecoder
import java.util.*

public class DashServerHttpHandlerOLD
@Inject
constructor(private val redisConnections: RedisConnections,
            private val authenticator: Authenticator,
            private val serviceManagerProvider: Provider<ServiceManager>,
            private val handlerFactory: ServerHandlerFactory,
            private val config: Config) : ServerHttpHandler()
{
    private val url = config.get("server.url", "ws://127.0.0.1")

    override fun messageReceived(ctx: ChannelHandlerContext,
                                 msg: FullHttpRequest)
    {
        val ch = ctx.channel()

        fun respond(status: HttpResponseStatus) {
            val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                    Unpooled.copiedBuffer(status.toString(), CharsetUtil.UTF_8))

            (ctx writeAndFlush response) addListener ChannelFutureListener.CLOSE
        }

        // check request validity
        if (msg.decoderResult().isFailure) {
            respond(HttpResponseStatus.BAD_REQUEST)
            return
        }

        val liveToken = getCookieValue(msg.headers(), "live_token")

        liveToken.ifPresent({ liveToken ->
            authenticator.authenticate(liveToken, { liveToken ->
                if (! (redisConnections addIfAllowed  liveToken.userId)) {
                    respond(HttpResponseStatus.TOO_MANY_REQUESTS)
                }
                else {
                    val wsFactory =
                            WebSocketServerHandshakerFactory(url, null, false)
                    val handshaker =
                            Optional.ofNullable(wsFactory.newHandshaker(msg))

                    handshaker.ifPresent({ handshaker ->
                        val wsHandler =
                                handlerFactory createServerWSHandler handshaker
                        val serviceManager = serviceManagerProvider.get()
                        val pipeline = ch.pipeline()

                        // set up ws pipeline
                        pipeline remove "httphandler"
                        pipeline addLast WebSocketServerCompressionHandler()
                        pipeline addLast wsHandler

                        // handshake; callback: start ServiceManager
                        handshaker.handshake(ch, msg) addListener
                                GenericFutureListener {
                                    serviceManager.start(ctx, liveToken)
                                }

                        // on channel close: stop ServiceManager
                        ch.closeFuture() addListener GenericFutureListener {
                            serviceManager.stop(ctx, liveToken)
                        }
                    }, {
                        WebSocketServerHandshakerFactory
                                .sendUnsupportedVersionResponse(ch)
                    })
                }
            }, { respond(HttpResponseStatus.FORBIDDEN) })
        }, { respond(HttpResponseStatus.FORBIDDEN) })
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable)
    {
        logThrowable(this, e)
        ctx.close()
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
