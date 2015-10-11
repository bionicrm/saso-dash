package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.server.DashServer;
import io.saso.dash.server.DashServerHttpHandler;
import io.saso.dash.server.DashServerInitializer;
import io.saso.dash.server.DashServerWSHandler;
import io.saso.dash.server.Server;
import io.saso.dash.server.ServerHttpHandler;
import io.saso.dash.server.ServerInitializer;
import io.saso.dash.server.ServerWSHandler;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
        bind(ServerInitializer.class).to(DashServerInitializer.class);

        bind(ServerHttpHandler.class).to(DashServerHttpHandler.class);
        bind(ServerWSHandler.class).to(DashServerWSHandler.class);
    }
}
