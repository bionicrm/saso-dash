package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import io.saso.dash.server.*;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
        bind(ServerInitializer.class).to(DashServerInitializer.class);

        bind(ServerHttpHandler.class).to(DashServerHttpHandler.class);

        install(new FactoryModuleBuilder()
                .implement(ServerWSHandler.class, DashServerWSHandler.class)
                .build(ServerFactory.class));
    }
}
