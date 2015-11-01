package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import io.saso.dash.startup.StartupManager;
import io.saso.dash.startup.StartupWorker;
import io.saso.dash.startup.impl.DashStartupManager;
import io.saso.dash.startup.workers.RedisStartupWorker;
import io.saso.dash.startup.workers.ServerStartupWorker;

public class StartupModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(StartupManager.class).to(DashStartupManager.class);
    }

    @Provides @Named("startup workers")
    StartupWorker[] provideStartupWorkers(
            RedisStartupWorker w0, ServerStartupWorker w1)
    {
        return new StartupWorker[] { w0, w1 };
    }
}
