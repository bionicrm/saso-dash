package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import io.netty.channel.ChannelHandler;
import io.saso.dash.server.Authentication;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.server.Server;
import io.saso.dash.server.handlers.http.*;
import io.saso.dash.server.handlers.ws.CloseFrameHandler;
import io.saso.dash.server.handlers.ws.PingFrameHandler;
import io.saso.dash.server.impl.DashAuthentication;
import io.saso.dash.server.impl.DashCookieFinder;
import io.saso.dash.server.impl.DashServer;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
        bind(CookieFinder.class).to(DashCookieFinder.class);
        bind(Authentication.class).to(DashAuthentication.class);
    }

    @Provides @Named("server http handlers")
    ChannelHandler[] provideHttpChannelHandlers(
            RequestValidationHandler h0, RequestMethodHandler h1,
            LiveTokenCookieHandler h2, LiveTokenEntityHandler h3,
            ConcurrentConnectionsHandler h4, UpgradeHandler h5)
    {
        return new ChannelHandler[] { h0, h1, h2, h3, h4, h5 };
    }

    @Provides @Named("server ws handlers")
    ChannelHandler[] provideWSChannelHandlers(
            PingFrameHandler h0, CloseFrameHandler h1)
    {
        return new ChannelHandler[] { h0, h1 };
    }
}
