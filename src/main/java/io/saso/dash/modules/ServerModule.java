package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import io.netty.channel.ChannelHandler;
import io.saso.dash.server.*;
import io.saso.dash.server.handlers.http.*;
import io.saso.dash.server.handlers.ws.CloseFrameHandler;
import io.saso.dash.server.handlers.ws.PingFrameHandler;
import io.saso.dash.server.handlers.ws.ServicesHandler;
import io.saso.dash.server.impl.DashAuthentication;
import io.saso.dash.server.impl.DashClient;
import io.saso.dash.server.impl.DashCookieFinder;
import io.saso.dash.server.impl.DashServer;
import me.mazeika.uconfig.Config;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
        bind(CookieFinder.class).to(DashCookieFinder.class);
        bind(Authentication.class).to(DashAuthentication.class);

        install(new FactoryModuleBuilder()
                .implement(Client.class, DashClient.class)
                .build(ClientFactory.class));
    }

    @Provides @Named("server http handlers")
    ChannelHandler[] provideHttpChannelHandlers(
            RequestValidationHandler h0, RequestMethodHandler h1,
            LiveTokenCookieHandler h2, LiveTokenEntityHandler h3,
            ConcurrentConnectionsHandler h4, HandshakeHandler h5,
            UpgradeHandler h6)
    {
        return new ChannelHandler[] { h0, h1, h2, h3, h4, h5, h6 };
    }

    @Provides @Named("server ws handlers")
    ChannelHandler[] provideWSChannelHandlers(
            PingFrameHandler h0, CloseFrameHandler h1, ServicesHandler h2)
    {
        return new ChannelHandler[] { h0, h1, h2 };
    }

    @Provides @Singleton
    Config provideConfig()
    {
        return Config.create("config.yaml", false);
    }
}
