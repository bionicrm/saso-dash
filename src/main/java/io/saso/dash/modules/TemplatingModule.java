package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.dash.templating.DashTemplater;
import io.dash.templating.Templater;

public class TemplatingModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Templater.class).to(DashTemplater.class);
    }
}
