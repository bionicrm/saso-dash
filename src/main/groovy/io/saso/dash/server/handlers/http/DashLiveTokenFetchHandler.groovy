package io.saso.dash.server.handlers.http
import com.google.inject.Inject
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.saso.dash.database.DBEntityFetcher
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBLiveToken

import io.saso.dash.util.HandlerUtil
import io.saso.dash.util.Resources

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ChannelHandler.Sharable
class DashLiveTokenFetchHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool()
    private static final String LIVE_TOKEN_SQL =
            Resources.get('/sql/live_token.sql')

    private final DBEntityFetcher entityFetcher
    private final DBEntityProviderFactory entityProviderFactory

    @Inject
    DashLiveTokenFetchHandler(DBEntityFetcher entityFetcher,
                              DBEntityProviderFactory entityProviderFactory)
    {
        this.entityFetcher = entityFetcher
        this.entityProviderFactory = entityProviderFactory
    }

    @Override
    void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {
        THREAD_POOL.execute {
            // get the live token cookie value of the request
            final Optional<String> liveTokenCookieValue =
                    getCookieValue(msg.headers(), 'live_token')

            // if the cookie value was found...
            if (liveTokenCookieValue.isPresent()) {
                // fetch the token from the DB
                final liveToken = entityFetcher.fetch(DBLiveToken,
                        LIVE_TOKEN_SQL, liveTokenCookieValue.get())

                // if the DB token was found...
                if (liveToken.isPresent()) {
                    // fire the next handler
                    ctx.fireChannelRead(msg)

                    final DBEntityProvider entityProvider =
                            entityProviderFactory.createDBEntityProvider(
                                    liveToken.get())

                    // fire the DBEntityProvider created user event
                    ctx.fireUserEventTriggered(entityProvider)

                    return
                }
            }

            // forbid the request
            HandlerUtil.sendResponseAndClose(ctx,
                    HttpResponseStatus.FORBIDDEN)
        }
    }

    private Optional<String> getCookieValue(HttpHeaders headers, String name)
    {
        headers.getAllAndConvert(HttpHeaderNames.COOKIE).each {
            final Cookie cookie = ClientCookieDecoder.decode(it)

            if (cookie.name() == name) {
                return Optional.of(URLDecoder.decode(cookie.value(), 'UTF-8'))
            }
        }

        return Optional.empty()
    }
}
