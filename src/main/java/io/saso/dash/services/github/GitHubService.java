package io.saso.dash.services.github;

import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;
import io.saso.dash.services.Service;
import org.kohsuke.github.GitHub;

public class GitHubService implements Service
{
    private ChannelHandlerContext ctx;
    private User user;
    private ProviderUser providerUser;
    private AuthToken authToken;

    private GitHub gitHub;

    @Override
    public void start() throws Exception
    {
        gitHub = GitHub.connectUsingOAuth(authToken.getAccess());
    }

    @Override
    public void poll() throws Exception
    {
        if (gitHub.isAnonymous()) {
            start();
            return;
        }

        // TODO
    }

    @Override
    public void stop() throws Exception
    {

    }

    @Override
    public int getPollInterval()
    {
        return 10;
    }

    @Override
    public String getProviderName()
    {
        return "github";
    }

    @Override
    public void setContext(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public void setProviderUser(ProviderUser providerUser)
    {
        this.providerUser = providerUser;
    }

    @Override
    public void setAuthToken(AuthToken authToken)
    {
        this.authToken = authToken;
    }
}
