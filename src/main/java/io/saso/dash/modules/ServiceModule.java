package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import io.saso.dash.services.*;
import io.saso.dash.services.github.GitHubNotificationSubService;
import io.saso.dash.services.github.GitHubService;
import io.saso.dash.services.google.GoogleService;

public class ServiceModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        // ServiceManager -> DashServiceManager
        bind(ServiceManager.class).to(DashServiceManager.class);

        install(new FactoryModuleBuilder()
                // DBEntityProvider -> DashDBEntityProvider
                .implement(DBEntityProvider.class, DashDBEntityProvider.class)

                        // @"github" Service -> GitHubService
                .implement(Service.class, Names.named("github"),
                        GitHubService.class)

                        // @"google" Service -> GoogleService
                .implement(Service.class, Names.named("google"),
                        GoogleService.class)

                .build(ServiceFactory.class));

        install(new FactoryModuleBuilder()
                // @"github-notification" Pollable ->
                //     GitHubNotificationSubService
                .implement(Pollable.class, Names.named("github-notification"),
                        GitHubNotificationSubService.class)

                .build(SubServiceFactory.class));
    }
}
