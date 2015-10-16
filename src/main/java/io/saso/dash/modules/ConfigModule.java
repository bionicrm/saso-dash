package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.dash.config.Config;
import io.dash.config.DashConfig;

public class ConfigModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Config.class).to(DashConfig.class);
    }
}
