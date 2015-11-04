package io.saso.dash.services.github

import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.services.services.Service
import io.saso.dash.services.ServiceLocal
import io.saso.dash.services.ServiceName
import io.saso.dash.services.github.subservices.GitHubNotificationSubService
import org.kohsuke.github.GitHub

class GitHubService implements Service
{
    final ServiceName name = ServiceName.GITHUB
    final Class<Service>[] subServices = [GitHubNotificationSubService]

    private final ServiceLocal serviceLocal

    @Inject
    GitHubService(ServiceLocal serviceLocal) {
        this.serviceLocal = serviceLocal
    }

    @Override
    void start(ChannelHandlerContext ctx, DBEntityProvider db)
    {
        serviceLocal.set(this, GitHub.connectUsingOAuth(
                db.getAuthToken(this).access))
    }
}
