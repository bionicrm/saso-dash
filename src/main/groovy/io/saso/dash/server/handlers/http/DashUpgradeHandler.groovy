package io.saso.dash.server.handlers.http

import com.google.inject.Inject
import com.google.inject.Singleton
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler
import io.saso.dash.config.Config
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.redis.databases.ConcurrentConnections
import io.saso.dash.server.events.ServerEventsFactory
import io.saso.dash.server.events.UpgradeRequestEvent
import io.saso.dash.server.handlers.ServerHandlersFactory
import io.saso.dash.util.HandlerUtil
import javafx.util.Pair

import java.sql.Timestamp
import java.time.Instant

@Singleton @ChannelHandler.Sharable
class DashUpgradeHandler extends ChannelHandlerAdapter
{
    private final ConcurrentConnections concurrentConnections
    private final ServerHandlersFactory serverHandlersFactory
    private final ServerEventsFactory serverEventsFactory
    private final String url

    @Inject
    DashUpgradeHandler(ConcurrentConnections concurrentConnections,
                       ServerHandlersFactory serverHandlersFactory,
                       ServerEventsFactory serverEventsFactory,
                       Config config)
    {
        this.concurrentConnections = concurrentConnections
        this.serverHandlersFactory = serverHandlersFactory
        this.serverEventsFactory = serverEventsFactory

        url = config.get('server.url', 'ws://127.0.0.1')
    }

    @Override
    void userEventTriggered(ChannelHandlerContext ctx, event)
    {
        final propagate = { ctx.fireUserEventTriggered(event) }

        if (event instanceof UpgradeRequestEvent) {
            final wsFactory =
                    new WebSocketServerHandshakerFactory(url, null, false)

            final handshaker = wsFactory.newHandshaker(event.request)

            if (handshaker == null) {
                WebSocketServerHandshakerFactory
                        .sendUnsupportedVersionResponse(ctx.channel())
            }
            else {
                final pipeline = ctx.channel().pipeline()

                // remove all pipeline handlers
                while (pipeline.size() > 0) {
                    pipeline.removeLast()
                }

                pipeline.addLast new WebSocketServerCompressionHandler()

                pipeline.addLast serverHandlersFactory
                        .createWSHandler(handshaker)

                handshaker.handshake(ctx.channel(), event.request).addListener {
                    propagate()
                }
            }
        }
        else {
            propagate()
        }
    }
}
