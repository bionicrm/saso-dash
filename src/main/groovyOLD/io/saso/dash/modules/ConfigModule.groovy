package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.config.Config
import io.saso.dash.config.impl.DashConfig

class ConfigModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Config to DashConfig
    }
}
