package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.server.impl.DashCookieFinder;
import io.saso.dash.server.impl.DashServer;
import io.saso.dash.server.Server;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(CookieFinder.class).to(DashCookieFinder.class);
        bind(Server.class).to(DashServer.class);
    }
}
