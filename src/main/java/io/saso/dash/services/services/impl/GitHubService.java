package io.saso.dash.services.services.impl;

import io.saso.dash.server.Client;
import io.saso.dash.services.services.Service;

public class GitHubService implements Service
{
    @Override
    public String getName()
    {
        return "github";
    }

    @Override
    public int getPollInterval()
    {
        return 30;
    }

    @Override
    public void start(Client client)
    {
        // TODO: implement
    }

    @Override
    public void poll(Client client)
    {
        // TODO: implement
    }

    @Override
    public void stop(Client client)
    {
        // TODO: implement
    }
}
