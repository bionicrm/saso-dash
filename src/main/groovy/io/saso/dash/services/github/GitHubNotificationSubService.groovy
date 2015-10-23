package io.saso.dash.services.github

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import groovy.json.JsonSlurper
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.redis.databases.RedisServices
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.SubServiceAdapter
import io.saso.dash.templating.TemplateRenderer
import org.kohsuke.github.GitHub

import java.util.concurrent.Future

class GitHubNotificationSubService extends SubServiceAdapter
{
    final TemplateRenderer templater
    final RedisServices redisServices
    final GitHub gitHub

    boolean stopping
    Future future

    @Inject
    GitHubNotificationSubService(TemplateRenderer templater,
                                 RedisServices redisServices,
                                 @Assisted GitHub gitHub)
    {
        this.templater = templater
        this.redisServices = redisServices
        this.gitHub = gitHub
    }

    @Override
    void start(ChannelHandlerContext ctx, DBEntityProvider db)
    {
        final itr = gitHub.listNotifications()
                .since(System.currentTimeMillis() - 60 * 60)
                .read(true)
                .iterator()

        final threadsFromRedis =
                redisServices.get(db.liveToken.userId, 'github')

        if (! threadsFromRedis.empty) {
            def json = new JsonSlurper()
            def threads = json.parseText(threadsFromRedis)

            for (thread in threads) {
                def rendered = templater.render()
            }
        }
    }
}
