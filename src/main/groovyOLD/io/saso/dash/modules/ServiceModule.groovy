package io.saso.dash.modules
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Named
import com.google.inject.name.Names
import io.saso.dash.services.DashServiceCreator
import io.saso.dash.services.DashServiceLocal
import io.saso.dash.services.Service
import io.saso.dash.services.ServiceCreator
import io.saso.dash.services.ServiceFactory
import io.saso.dash.services.ServiceLocal
import io.saso.dash.services.github.GitHubService
import io.saso.dash.services.google.GoogleService

class ServiceModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind ServiceCreator to DashServiceCreator
        bind ServiceLocal to DashServiceLocal

        install(new FactoryModuleBuilder()
                .implement(Service, Names.named('github'), GitHubService)
                .implement(Service, Names.named('google'), GoogleService)
                .build(ServiceFactory))
    }
}
