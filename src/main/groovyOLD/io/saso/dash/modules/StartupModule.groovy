package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Named
import io.saso.dash.startup.DashStartupPipeline
import io.saso.dash.startup.StartupPipeline
import io.saso.dash.startup.StartupWorker
import io.saso.dash.startup.workers.RedisWorker

class StartupModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind StartupPipeline to DashStartupPipeline
    }

    @Provides @Named('startup workers')
    List<StartupWorker> provideStartupWorkers(RedisWorker redis)
    {
        [redis]
    }
}
