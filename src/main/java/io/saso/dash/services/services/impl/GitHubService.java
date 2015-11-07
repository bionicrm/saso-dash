package io.saso.dash.services.services.impl;

import io.saso.dash.server.Client;
import io.saso.dash.services.services.Service;
import org.kohsuke.github.GHThread;
import org.kohsuke.github.GitHub;

import java.util.Iterator;

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
        // TODO: we don't want read messages
        Iterator<GHThread> threads = gitHub.listNotifications()
                .nonBlocking(true).read(true).iterator();

        while (threads.hasNext()) {
            final GHThread thread = threads.next();


        }
    }

    @Override
    public void stop(Client client)
    {
        // TODO: implement
    }
}
