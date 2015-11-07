package io.saso.dash.startup.workers;

import com.google.inject.Inject;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.ServiceStorage;
import io.saso.dash.startup.StartupWorker;

public class RedisStartupWorker implements StartupWorker
{
    private final ConcurrentConnections concurrentConnections;
    private final ServiceStorage serviceStorage;

    @Inject
    public RedisStartupWorker(ConcurrentConnections concurrentConnections,
                              ServiceStorage serviceStorage)
    {
        this.concurrentConnections = concurrentConnections;
        this.serviceStorage = serviceStorage;
    }

    @Override
    public void onStart()
    {
        concurrentConnections.initialize();
        serviceStorage.initialize();
    }
}
