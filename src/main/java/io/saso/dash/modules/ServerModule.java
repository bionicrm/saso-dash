package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import io.netty.channel.ChannelHandler;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.server.impl.DashCookieFinder;
import io.saso.dash.server.impl.DashServer;
import io.saso.dash.server.Server;
import io.saso.dash.server.pipeline.handlers.HandlerFactory;
import io.saso.dash.server.pipeline.handlers.RequestMethodHandler;
import io.saso.dash.server.pipeline.handlers.RequestValidationHandler;
import io.saso.dash.server.pipeline.handlers.UpgradingHandler;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(CookieFinder.class).to(DashCookieFinder.class);
        bind(Server.class).to(DashServer.class);

        // pipeline.handlers
        install(new FactoryModuleBuilder()
                .implement(ChannelHandler.class,
                        Names.named("RequestMethod"),
                        RequestMethodHandler.class)
                .implement(ChannelHandler.class,
                        Names.named("RequestValidation"),
                        RequestValidationHandler.class)
                .implement(ChannelHandler.class,
                        Names.named("Upgrading"),
                        UpgradingHandler.class)
                .build(HandlerFactory.class));
    }
}
