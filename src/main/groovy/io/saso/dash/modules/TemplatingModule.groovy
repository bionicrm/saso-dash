package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.templating.DashTemplater
import io.saso.dash.templating.Templater

class TemplatingModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Templater to DashTemplater
    }
}
