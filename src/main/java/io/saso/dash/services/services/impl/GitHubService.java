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
        return 10;
    }

    @Override
    public void start(Client client)
    {

    }

    @Override
    public void poll(Client client)
    {

    }

    @Override
    public void stop(Client client)
    {

    }
}
