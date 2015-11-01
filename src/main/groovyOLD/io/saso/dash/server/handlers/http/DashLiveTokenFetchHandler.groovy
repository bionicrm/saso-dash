package io.saso.dash.server.handlers.http
import com.google.inject.Inject
import com.google.inject.Singleton
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.ReferenceCountUtil
import io.saso.dash.database.DBFetcher
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.server.events.ServerEventsFactory
import io.saso.dash.util.HandlerUtil
import io.saso.dash.util.Resources

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Singleton @ChannelHandler.Sharable
class DashLiveTokenFetchHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool()
    private static final String LIVE_TOKEN_SQL =
            Resources.get('/sql/find_live_token.sql')

    private final DBFetcher entityFetcher
    private final DBEntityProviderFactory entityProviderFactory
    private final ServerEventsFactory serverEventsFactory

    @Inject
    DashLiveTokenFetchHandler(DBFetcher entityFetcher,
                              DBEntityProviderFactory entityProviderFactory,
                              ServerEventsFactory serverEventsFactory)
    {
        this.entityFetcher = entityFetcher
        this.entityProviderFactory = entityProviderFactory
        this.serverEventsFactory = serverEventsFactory
    }

    @Override
    void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {
        msg.retain()

        final propagate = { ctx.fireChannelRead(msg) }

        final forbid = {
            HandlerUtil.sendResponseAndClose(ctx, HttpResponseStatus.FORBIDDEN)
            msg.release()
        }

        final getCookieValue = { String name ->
            String value = ''

            msg.headers().getAllAndConvert(HttpHeaderNames.COOKIE).each {
                final Cookie cookie = ClientCookieDecoder.decode(it)

                if (cookie.name() == name) {
                    return value = URLDecoder.decode(cookie.value(), 'UTF-8')
                }
            }

            return value
        }

        THREAD_POOL.execute {
            final liveTokenCookieValue = getCookieValue('live_token')

            if (liveTokenCookieValue.empty) {
                forbid()
            }
            else {
                final liveToken = entityFetcher.fetch(DBLiveToken,
                        LIVE_TOKEN_SQL, liveTokenCookieValue)

                if (liveToken.present) {
                    final entityProvider = entityProviderFactory
                            .createDBEntityProvider(liveToken.get())

                    final event = serverEventsFactory
                            .createUpgradeRequestEvent(msg, entityProvider)

                    // fire event
                    ctx.fireUserEventTriggered(event)

                    propagate()
                }
                else {
                    forbid()
                }
            }
        }
    }
}
