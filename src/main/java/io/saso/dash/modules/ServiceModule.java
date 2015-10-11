package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import io.saso.dash.services.*;
import io.saso.dash.services.github.GitHubService;
import io.saso.dash.services.google.GoogleService;

public class ServiceModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ServiceManager.class).to(DashServiceManager.class);

        bind(Service.class)
                .annotatedWith(Names.named("github"))
                .to(GitHubService.class);
        bind(Service.class)
                .annotatedWith(Names.named("google"))
                .to(GoogleService.class);
    }
}
