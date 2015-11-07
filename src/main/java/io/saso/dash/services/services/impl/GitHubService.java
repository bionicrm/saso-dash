package io.saso.dash.services.services.impl;

import io.saso.dash.server.Client;
import io.saso.dash.services.services.Service;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class GitHubService implements Service
{
    private GitHub gitHub;

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
    public void start(Client client) throws Exception
    {
        gitHub = GitHub.connectUsingOAuth(client.authToken(this).getAccess());
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
