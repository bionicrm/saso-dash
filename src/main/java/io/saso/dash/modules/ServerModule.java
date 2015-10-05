package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.server.*;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Server.class).to(DashServer.class);
        bind(ServerInitializer.class).to(DashServerInitializer.class);
        bind(ServerHandler.class).to(DashServerHandler.class);
    }
}
