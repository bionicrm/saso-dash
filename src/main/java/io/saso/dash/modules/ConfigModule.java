package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.config.Config;
import io.saso.config.DashConfig;

public class ConfigModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Config.class).to(DashConfig.class);
    }
}
