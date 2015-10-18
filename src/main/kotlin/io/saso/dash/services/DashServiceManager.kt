package io.saso.dash.services

import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.redis.databases.RedisConnections
import io.saso.dash.util.schedulingPool
import io.saso.dash.util.threadPool
import java.util.*

import java.util.concurrent.*
import kotlin.properties.Delegates

public class DashServiceManager
@Inject
constructor(/* TODO: use preferences */
        private val redisConnections: RedisConnections,
        private val serviceFactory: ServiceFactory) : ServiceManager
{
    private val services: List<Service> = listOf(
            serviceFactory.createGitHubService(),
            serviceFactory.createGoogleService())
    private val serviceSchedules: MutableMap<Service, ScheduledFuture<*>> =
            HashMap()

    private var db: DBEntityProvider by Delegates.notNull()

    override fun start(ctx: ChannelHandlerContext, liveToken: LiveToken)
    {
        db = serviceFactory createDBEntityProvider liveToken

        threadPool.execute {
            services.forEach {
                it.start(ctx, db)
            }
        }

        services.forEach {
            val interval = it.pollInterval.toLong()

            serviceSchedules.put(it, schedulingPool.scheduleAtFixedRate({
                threadPool.execute {
                    it.poll(ctx, db)
                }
            }, interval, interval, TimeUnit.SECONDS))
        }
    }

    override fun stop(ctx: ChannelHandlerContext, liveToken: LiveToken)
    {
        threadPool.execute {
            services.forEach {
                serviceSchedules.get(it)?.cancel(true)

                it.stop(ctx, db)
            }

            redisConnections remove liveToken.userId
        }
    }
}
