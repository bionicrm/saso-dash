package io.saso.dash.services
import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.EntityProvider
import io.saso.dash.database.EntityProviderFactory
import io.saso.dash.database.entities.LiveToken

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class DashServiceScheduler implements ServiceScheduler
{
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool()
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor()

    private final EntityProviderFactory entityProviderFactory
    private final Set<Service> services
    private final Map<Service, ScheduledFuture> scheduledFutures = new HashMap()

    @Inject
    DashServiceScheduler(EntityProviderFactory entityProviderFactory,
                         ServiceCreator serviceCreator)
    {
        this.entityProviderFactory = entityProviderFactory

        services = serviceCreator.createServices()
    }

    @Override
    void schedule(ChannelHandlerContext ctx, LiveToken liveToken)
    {
        final EntityProvider entityProvider =
                entityProviderFactory.createDBEntityProvider liveToken

        THREAD_POOL.execute {
            services.forEach {
                it.start ctx, entityProvider
            }
        }

        services.forEach { service ->
            final interval = service.pollInterval

            if (interval != -1) {
                scheduledFutures[service] = SCHEDULER.scheduleAtFixedRate({
                    THREAD_POOL.execute {
                        service.poll ctx, entityProvider
                    }
                }, interval, interval, TimeUnit.SECONDS)
            }
        }
    }

    @Override
    void cancel(ChannelHandlerContext ctx, LiveToken liveToken)
    {
        THREAD_POOL.execute {
            services.forEach { service ->
                scheduledFutures[service].cancel true

                service.stop ctx
            }
        }
    }
}
