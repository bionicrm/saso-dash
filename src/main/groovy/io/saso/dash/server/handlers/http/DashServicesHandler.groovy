package io.saso.dash.server.handlers.http
import com.google.inject.Inject
import com.google.inject.name.Named
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.server.events.UpgradeRequestEvent
import io.saso.dash.services.Service
import io.saso.dash.services.ServiceCreator

import java.util.concurrent.*

class DashServicesHandler extends ChannelHandlerAdapter
{
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool()
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor()

    private final ServiceCreator serviceCreator
    private final Map<Service, ScheduledFuture> scheduledFutures =
            new HashMap()

    private DBEntityProvider entityProvider
    private Set<Service> services

    @Inject
    DashServicesHandler(ServiceCreator serviceCreator)
    {
        this.serviceCreator = serviceCreator
    }

    @Override
    void userEventTriggered(ChannelHandlerContext ctx, event)
    {
        final propagate = { ctx.fireUserEventTriggered(event) }

        if (event instanceof UpgradeRequestEvent) {
            entityProvider = event.entityProvider

            services = serviceCreator.createServices()

            THREAD_POOL.execute {
                services.each { it.start(ctx, entityProvider) }
            }

            services.each { service ->
                if (service.pollTime != -1) {
                    assert service.pollTime > 0

                    scheduledFutures[service] = SCHEDULER.scheduleAtFixedRate({
                        THREAD_POOL.execute {
                            service.poll(ctx, entityProvider)
                        }
                    }, service.pollTime, service.pollTime, TimeUnit.SECONDS)
                }
            }
        }
        else {
            propagate()
        }
    }

    @Override
    void close(ChannelHandlerContext ctx, ChannelPromise promise)
    {
        final propagate = { ctx.close(promise) }

        THREAD_POOL.execute {
            services.each { service ->
                service.stop(ctx, entityProvider)
                scheduledFutures[service].cancel(true)
            }
        }

        propagate()
    }
}
