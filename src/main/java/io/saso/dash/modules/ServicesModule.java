package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import io.saso.dash.services.Service;
import io.saso.dash.services.ServiceScheduler;
import io.saso.dash.services.impl.DashServiceScheduler;
import io.saso.dash.services.impl.GitHubService;

public class ServicesModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ServiceScheduler.class).to(DashServiceScheduler.class);
    }

    @Provides @Named("services")
    Service[] provideServices(GitHubService s0)
    {
        return new Service[] { s0 };
    }
}
