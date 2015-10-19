package io.saso.dash.services.github

import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.Service
import io.saso.dash.services.SubServiceFactory
import io.saso.dash.util.logger
import org.kohsuke.github.GitHub

public class GitHubService
@Inject
constructor(private val subServiceFactory: SubServiceFactory) : Service()
{
    public override val pollInterval = -1

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        val gitHub = GitHub.connectUsingOAuth(db.authToken("github").access)

        // notification
        registerSubPollable(
                subServiceFactory.createGitHubNotificationSubService(gitHub))

        super.start(ctx, db)
    }
}
