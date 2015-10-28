package io.saso.dash.modules;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.saso.dash.config.Config;
import io.saso.dash.config.ConfigModel;
import io.saso.dash.config.DashConfig;

public class GeneralModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Gson.class).toInstance(new Gson());
    }
}
