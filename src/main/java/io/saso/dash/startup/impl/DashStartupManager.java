package io.saso.dash.startup.impl;

import com.google.inject.Inject;
import io.saso.dash.startup.StartupManager;
import io.saso.dash.startup.StartupWorker;
import io.saso.dash.startup.workers.RedisStartupWorker;

public class DashStartupManager implements StartupManager
{
    private final StartupWorker[] workers;

    @Inject
    public DashStartupManager(RedisStartupWorker redis)
    {
        workers = new StartupWorker[] { redis };
    }

    @Override
    public void start()
    {
        for (StartupWorker worker : workers) {
            worker.onStart();
        }
    }
}
