package io.saso.dash.services;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.database.entities.LiveToken;
import io.saso.dash.redis.databases.RedisConnections;
import io.saso.dash.util.LoggingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DashServiceManager implements ServiceManager
{
    private static final ExecutorService CACHED_THREAD_POOL =
            Executors.newCachedThreadPool();
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR =
            Executors.newSingleThreadScheduledExecutor();

    private final RedisConnections redisConnections;
    private final List<Service> services = new ArrayList<>();
    private final Map<Service, ScheduledFuture> serviceSchedules =
            new HashMap<>();
    private final ServiceFactory serviceFactory;

    private DBEntityProvider db;

    @Inject
    public DashServiceManager(/* TODO: use preferences */
                              RedisConnections redisConnections,
                              ServiceFactory serviceFactory)
    {
        this.redisConnections = redisConnections;
        this.serviceFactory = serviceFactory;
    }

    @Override
    public void start(ChannelHandlerContext ctx, LiveToken liveToken)
            throws Exception
    {
        db = serviceFactory.createDBEntityProvider(liveToken);

        services.add(serviceFactory.createGitHubService());
        services.add(serviceFactory.createGoogleService());

        // in thread pool...
        CACHED_THREAD_POOL.execute(() ->
                // for each service...
                services.forEach(service ->
                        // start it
                        executeChecked(() -> service.start(ctx, db))));

        // for each service...
        services.forEach(service -> {
            final long interval = service.getPollInterval();

            // add a schedule for the service...
            serviceSchedules.put(service,
                    // a new schedule...
                    SCHEDULED_EXECUTOR.scheduleAtFixedRate(() ->
                        // that executes in a thread pool...
                        CACHED_THREAD_POOL.execute(() ->
                            // poll the service
                            executeChecked(() -> service.poll(ctx, db)))
                    , interval, interval, TimeUnit.SECONDS));
        });
    }

    @Override
    public void stop(ChannelHandlerContext ctx, LiveToken liveToken)
    {
        // in thread pool...
        CACHED_THREAD_POOL.execute(() -> {
            // for each service...
            services.forEach(service -> {
                // cancel the service's schedule
                serviceSchedules.get(service).cancel(true);

                // and stop the schedule
                executeChecked(() -> service.stop(ctx, db));
            });

            // remove concurrent connection for user
            redisConnections.remove(liveToken.getUserId());
        });
    }

    private void executeChecked(RunnableThrowable runnableThrowable)
    {
        try {
            runnableThrowable.run();
        }
        catch (Exception e) {
            LoggingUtil.logThrowable(e, getClass());
        }
    }

    private interface RunnableThrowable
    {
        void run() throws Exception;
    }
}
