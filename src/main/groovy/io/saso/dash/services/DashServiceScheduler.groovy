package io.saso.dash.services
import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBLiveToken

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

    private final DBEntityProviderFactory entityProviderFactory
    private final Set<Service> services
    private final Map<Service, ScheduledFuture> scheduledFutures = new HashMap()

    @Inject
    DashServiceScheduler(DBEntityProviderFactory entityProviderFactory,
                         ServiceCreator serviceCreator)
    {
        this.entityProviderFactory = entityProviderFactory

        services = serviceCreator.createServices()
    }

    @Override
    void schedule(ChannelHandlerContext ctx, DBLiveToken liveToken)
    {
        final DBEntityProvider entityProvider =
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
    void cancel(ChannelHandlerContext ctx, DBLiveToken liveToken)
    {
        THREAD_POOL.execute {
            services.forEach { service ->
                service.stop ctx

                scheduledFutures[service].cancel true
            }
        }
    }
}
