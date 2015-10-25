package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Named
import com.google.inject.name.Names
import io.netty.channel.ChannelHandler

import io.saso.dash.server.DashServer

import io.saso.dash.server.Server
import io.saso.dash.server.handlers.http.DashLiveTokenFetchHandler
import io.saso.dash.server.handlers.DashServerWSHandler

import io.saso.dash.server.DashChannelInitializer
import io.saso.dash.server.handlers.ServerHandlerFactory
import io.saso.dash.server.handlers.ServerHttpHandler
import io.saso.dash.server.handlers.ServerWSHandler
import io.saso.dash.server.handlers.http.DashLiveTokenValidationHandler
import io.saso.dash.server.handlers.http.DashRequestValidationHandler
import io.saso.dash.server.handlers.http.DashUserLimitsHandler

class ServerModule extends AbstractModule
{
    @Override
    void configure() {
        bind Server to DashServer

        bind(ChannelHandler).annotatedWith(Names.named('ch init'))
                .to(DashChannelInitializer)

        install new FactoryModuleBuilder()
                // ServerWSHandler -> DashServerWSHandler
                .implement(ServerWSHandler, DashServerWSHandler)

                .build(ServerHandlerFactory)
    }

    @Provides @Named('http handlers')
    List<ChannelHandler> provideHttpHandlers(
            DashLiveTokenFetchHandler liveTokenFetch,
            DashLiveTokenValidationHandler liveTokenValidation,
            DashRequestValidationHandler requestValidation,
            DashUserLimitsHandler userLimits)
    {
        return [requestValidation, liveTokenFetch, liveTokenValidation,
                userLimits]
    }
}
