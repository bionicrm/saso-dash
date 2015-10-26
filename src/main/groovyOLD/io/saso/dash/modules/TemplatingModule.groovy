package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.templating.DashTemplateRenderer
import io.saso.dash.templating.TemplateRenderer

class TemplatingModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind TemplateRenderer to DashTemplateRenderer
    }
}
