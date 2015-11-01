package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Named
import com.google.inject.name.Names
import io.netty.channel.ChannelHandler

import io.saso.dash.server.impl.DashServer

import io.saso.dash.server.Server
import io.saso.dash.server.events.DashUpgradeRequestEvent
import io.saso.dash.server.events.ServerEventsFactory
import io.saso.dash.server.events.UpgradeRequestEvent
import io.saso.dash.server.handlers.ServerHandlersFactory
import io.saso.dash.server.handlers.http.DashLiveTokenFetchHandler

import io.saso.dash.server.handlers.http.DashLiveTokenValidationHandler
import io.saso.dash.server.handlers.http.DashRequestValidationHandler
import io.saso.dash.server.handlers.http.DashServicesHandler
import io.saso.dash.server.handlers.http.DashUpgradeHandler
import io.saso.dash.server.handlers.http.DashUserLimitsHandler
import io.saso.dash.server.handlers.ws.DashWSHandler

class ServerModule extends AbstractModule
{
    @Override
    void configure() {
        bind Server to DashServer

        bind(ChannelHandler).annotatedWith(Names.named('ch init'))
                .to(DashChannelInitializer)

        // events
        install new FactoryModuleBuilder()
                .implement(UpgradeRequestEvent, DashUpgradeRequestEvent)
                .build(ServerEventsFactory)

        // handlers
        install new FactoryModuleBuilder()
                .implement(ChannelHandler, Names.named('ws handler'),
                           DashWSHandler)
                .build(ServerHandlersFactory)
    }

    @Provides @Named('http handlers')
    List<ChannelHandler> provideHttpHandlers(
            DashLiveTokenFetchHandler liveTokenFetch,
            DashLiveTokenValidationHandler liveTokenValidation,
            DashRequestValidationHandler requestValidation,
            DashServicesHandler services,
            DashUpgradeHandler upgrade,
            DashUserLimitsHandler userLimits)
    {
        return [requestValidation, liveTokenFetch, liveTokenValidation,
                userLimits, upgrade, services]
    }
}
