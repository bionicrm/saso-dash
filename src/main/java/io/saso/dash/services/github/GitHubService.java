package io.saso.dash.services.github;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;
import io.saso.dash.services.Service;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.util.Map;

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
        for (Map.Entry<String, GHRepository> repo : gitHub.getMyself().getAllRepositories().entrySet()) {
            try {
                final String collabs = repo.getValue().getCollaboratorNames().stream().reduce((s, s2) -> s + ", " + s2).get();

                ctx.channel().writeAndFlush(new TextWebSocketFrame(
                        "Repo " + repo.getKey() + " by " + repo.getValue().getOwnerName() + " with collaberators: " + collabs));
            } catch (Exception e) { /* empty */ }
        }
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
