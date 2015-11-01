package io.saso.dash.startup.workers;

import com.google.inject.Inject;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.startup.StartupWorker;

public class RedisStartupWorker implements StartupWorker
{
    private final ConcurrentConnections concurrentConnections;

    @Inject
    public RedisStartupWorker(ConcurrentConnections concurrentConnections)
    {
        this.concurrentConnections = concurrentConnections;
    }

    @Override
    public void onStart()
    {
        concurrentConnections.initialize();
    }
}
