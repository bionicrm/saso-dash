package io.saso.dash.services

import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.redis.databases.RedisConnections
import io.saso.dash.util.SCHEDULING_POOL
import io.saso.dash.util.THREAD_POOL
import java.util.*

import java.util.concurrent.*
import kotlin.properties.Delegates

public class DashServiceManagerOLD
@Inject
constructor(/* TODO: use preferences */
        private val redisConnections: RedisConnections,
        private val serviceFactory: DBEntityProviderFactory) : ServiceManager
{
    private val services: List<Service> = listOf(
            serviceFactory.createGitHubService(),
            serviceFactory.createGoogleService())
    private val serviceSchedules: MutableMap<Service, ScheduledFuture<*>> =
            HashMap()

    private var db: DBEntityProvider by Delegates.notNull()

    override fun start(ctx: ChannelHandlerContext, liveToken: DBLiveToken)
    {
        db = serviceFactory createDBEntityProvider liveToken

        THREAD_POOL.execute {
            services.forEach {
                it.start(ctx, db)
            }
        }

        services.forEach {
            val interval = it.pollInterval.toLong()

            if (interval != -1L) {
                serviceSchedules.put(it, SCHEDULING_POOL.scheduleAtFixedRate({
                    THREAD_POOL.execute {
                        it.poll(ctx, db)
                    }
                }, interval, interval, TimeUnit.SECONDS))
            }
        }
    }

    override fun stop(ctx: ChannelHandlerContext, liveToken: DBLiveToken)
    {
        THREAD_POOL.execute {
            services.forEach {
                serviceSchedules.get(it)?.cancel(true)

                it.stop(ctx)
            }

            redisConnections remove liveToken.userId
        }
    }
}
