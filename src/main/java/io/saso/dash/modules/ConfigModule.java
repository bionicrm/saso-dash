package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.saso.dash.config.Config;
import io.saso.dash.config.ConfigModel;
import io.saso.dash.config.DashConfig;

public class ConfigModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Config.class).to(DashConfig.class);
    }

    @Provides
    ConfigModel provideConfigModel(Config config)
    {
        return config.getModel();
    }
}
