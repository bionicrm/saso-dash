package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Names
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.DashDBEntityProvider
import io.saso.dash.services.DashServiceManager
import io.saso.dash.services.Service
import io.saso.dash.services.ServiceFactory
import io.saso.dash.services.ServiceManager
import io.saso.dash.services.ServicePollable
import io.saso.dash.services.SubServiceFactory
import io.saso.dash.services.github.GitHubNotificationSubService
import io.saso.dash.services.github.GitHubService
import io.saso.dash.services.google.GoogleService

class ServiceModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind ServiceManager to DashServiceManager

        install new FactoryModuleBuilder()
                // DBEntityProvider -> DashDBEntityProvider
                .implement(DBEntityProvider, DashDBEntityProvider)
                // @'github' Service -> GitHubService
                .implement(Service, Names.named('github'), GitHubService)
                // @'google' Service -> GoogleService
                .implement(Service, Names.named('google'), GoogleService)

                .build(ServiceFactory)

        install new FactoryModuleBuilder()
                // @'github-notification' ServicePollable ->
                // GitHubNotificationSubService
                .implement(ServicePollable, Names.named('github-notification'),
                    GitHubNotificationSubService)

                .build(SubServiceFactory)
    }
}
