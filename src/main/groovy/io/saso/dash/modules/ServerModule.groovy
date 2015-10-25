package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
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

class ServerModule extends AbstractModule
{
    @Override
    void configure() {
        bind Server to DashServer

        bind(ChannelHandler).annotatedWith(Names.named('ch init'))
                .to(DashChannelInitializer)

        // handlers
        bind ServerHttpHandler to DashLiveTokenFetchHandler

        bind ContextLocal to DashContextLocal

        install new FactoryModuleBuilder()
                // ServerWSHandler -> DashServerWSHandler
                .implement(ServerWSHandler, DashServerWSHandler)

                .build(ServerHandlerFactory)
    }
}
