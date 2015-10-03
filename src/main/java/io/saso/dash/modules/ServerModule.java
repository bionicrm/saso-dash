package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.saso.dash.server.*;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(int.class).annotatedWith(Names.named("port")).toInstance(8080);

        bind(Server.class).to(DashServer.class);
        bind(ServerHandler.class).to(DashServerHandler.class);
        bind(ServerInitializer.class).to(DashServerInitializer.class);
    }
}
