package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.services.ServiceScheduler;
import io.saso.dash.services.impl.DashServiceScheduler;

public class ServicesModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ServiceScheduler.class).to(DashServiceScheduler.class);
    }
}
