package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.templating.TemplateRenderer;
import io.saso.dash.templating.impl.DashTemplateRenderer;

public class TemplatingModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(TemplateRenderer.class).to(DashTemplateRenderer.class);
    }
}
