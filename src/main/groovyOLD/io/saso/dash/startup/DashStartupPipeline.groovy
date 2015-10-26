package io.saso.dash.startup

import com.google.inject.Inject
import com.google.inject.name.Named

class DashStartupPipeline implements StartupPipeline
{
    private final List<StartupWorker> workers

    @Inject
    DashStartupPipeline(@Named('startup workers') List<StartupWorker> workers)
    {
        this.workers = workers
    }

    @Override
    void run()
    {
        workers.each { it.work() }
    }
}
