package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import io.netty.channel.ChannelHandler;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.server.Server;
import io.saso.dash.server.impl.DashCookieFinder;
import io.saso.dash.server.impl.DashServer;
import io.saso.dash.server.pipeline.handlers.HandlerFactory;
import io.saso.dash.server.pipeline.handlers.RequestMethodHandler;
import io.saso.dash.server.pipeline.handlers.RequestValidationHandler;
import io.saso.dash.server.pipeline.handlers.UpgradingHandler;
import io.saso.dash.startup.StartupManager;
import io.saso.dash.startup.impl.DashStartupManager;

public class StartupModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(StartupManager.class).to(DashStartupManager.class);
    }
}
