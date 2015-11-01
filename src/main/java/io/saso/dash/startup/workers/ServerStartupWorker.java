package io.saso.dash.startup.workers;

import com.google.inject.Inject;
import io.saso.dash.server.Server;
import io.saso.dash.startup.StartupWorker;

public class ServerStartupWorker implements StartupWorker
{
    private final Server server;

    @Inject
    public ServerStartupWorker(Server server)
    {
        this.server = server;
    }

    @Override
    public void onStart()
    {
        server.start();
    }
}
