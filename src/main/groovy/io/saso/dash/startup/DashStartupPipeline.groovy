package io.saso.dash.startup

import com.google.inject.Inject
import com.google.inject.name.Named
import io.saso.dash.Worker

class DashStartupPipeline implements StartupPipeline
{
    private final List<Worker> workers

    @Inject
    DashStartupPipeline(@Named('startup workers') List<Worker> workers)
    {
        this.workers = workers
    }

    @Override
    void start()
    {
        workers.forEach { worker ->
            worker.work()
        }
    }
}
