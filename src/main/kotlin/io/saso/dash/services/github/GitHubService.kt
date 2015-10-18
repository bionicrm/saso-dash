package io.saso.dash.services.github

import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.Service
import io.saso.dash.services.SubServiceFactory
import org.kohsuke.github.GitHub

public class GitHubService
@Inject
constructor(private val subServiceFactory: SubServiceFactory) : Service()
{
    public override val providerName = "github"
    public override val pollInterval = 10

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        val gitHub = GitHub.connectUsingOAuth(db.authToken(this).access)

        // notification
        registerSubPollable(
                subServiceFactory.createGitHubNotificationSubService(
                        this, gitHub))

        super.start(ctx, db)
    }
}
