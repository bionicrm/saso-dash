package io.saso.dash.startup.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.saso.dash.startup.StartupManager;
import io.saso.dash.startup.StartupWorker;

public class DashStartupManager implements StartupManager
{
    private final StartupWorker[] workers;

    @Inject
    public DashStartupManager(@Named("startup workers") StartupWorker[] workers)
    {
        this.workers = workers;
    }

    @Override
    public void start()
    {
        for (StartupWorker worker : workers) {
            worker.onStart();
        }
    }
}
