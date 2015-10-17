package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.saso.dash.server.DashServer
import io.saso.dash.server.Server

import io.saso.dash.server.DashServerInitializer
import io.saso.dash.server.handlers.DashServerHttpHandler
import io.saso.dash.server.handlers.DashServerWSHandler

import io.saso.dash.server.ServerInitializer
import io.saso.dash.server.handlers.ServerHandlerFactory
import io.saso.dash.server.handlers.ServerHttpHandler
import io.saso.dash.server.handlers.ServerWSHandler

class ServerModule extends AbstractModule
{
    @Override
    void configure() {
        bind Server to DashServer
        bind ServerInitializer to DashServerInitializer

        // handlers
        bind ServerHttpHandler to DashServerHttpHandler

        install new FactoryModuleBuilder()
                // ServerWSHandler -> DashServerWSHandler
                .implement(ServerWSHandler, DashServerWSHandler)

                .build(ServerHandlerFactory)
    }
}
