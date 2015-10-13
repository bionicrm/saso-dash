package io.saso.dash.services.github;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.services.DBEntityProvider;
import io.saso.dash.services.Service;
import io.saso.dash.services.SubServiceFactory;
import org.kohsuke.github.GitHub;

public class GitHubService extends Service
{
    private final SubServiceFactory subServiceFactory;

    @Inject
    public GitHubService(SubServiceFactory subServiceFactory)
    {
        this.subServiceFactory = subServiceFactory;
    }

    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        final GitHub gitHub =
                GitHub.connectUsingOAuth(db.authToken(this).getAccess());

        // notification
        registerSubPollable(
                subServiceFactory.createGitHubNotificationSubService(
                        this, gitHub));

        super.start(ctx, db);
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
}
