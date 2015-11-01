package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.startup.StartupManager;
import io.saso.dash.startup.impl.DashStartupManager;

public class StartupModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(StartupManager.class).to(DashStartupManager.class);
    }
}
