package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.server.DashServer;
import io.saso.dash.server.Server;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
    }
}
