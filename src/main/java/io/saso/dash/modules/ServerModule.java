package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.netty.channel.ChannelHandler;
import io.saso.dash.binding_annotations.ChInit;
import io.saso.dash.server.DashChannelInitializer;
import io.saso.dash.server.DashServer;
import io.saso.dash.server.Server;

public class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ChannelHandler.class)
                .annotatedWith(ChInit.class)
                .to(DashChannelInitializer.class);

        bind(Server.class).to(DashServer.class);
    }
}
