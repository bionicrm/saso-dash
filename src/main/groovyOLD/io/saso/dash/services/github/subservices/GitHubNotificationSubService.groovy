package io.saso.dash.services.github.subservices

import com.google.gson.Gson
import com.google.inject.Inject
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.services.services.Service
import io.saso.dash.services.ServiceLocal
import io.saso.dash.services.ServiceName
import io.saso.dash.templating.Template
import io.saso.dash.templating.TemplateRenderer
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.NotImplementedException
import org.kohsuke.github.GHThread
import org.kohsuke.github.GitHub
import org.pegdown.PegDownProcessor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class GitHubNotificationSubService implements Service
{
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool()

    final ServiceName name = ServiceName.GITHUB

    private final TemplateRenderer templateRenderer
    private final GitHub gitHub

    private Future future
    private boolean stop

    @Inject
    GitHubNotificationSubService(TemplateRenderer templateRenderer,
                                 ServiceLocal serviceLocal)
    {
        this.templateRenderer = templateRenderer

        gitHub = serviceLocal.<GitHub>get this
    }

    @Override
    void start(ChannelHandlerContext ctx, DBEntityProvider db)
    {
        final itr = gitHub.listNotifications()
                .since(System.currentTimeMillis() - 60 * 60)
                .read(true)
                .iterator()

        println 'GitHubNotificationSubService started'

        future = THREAD_POOL.submit {
            while (! stop) {
                onNotification(ctx, db, itr.next())
            }
        }
    }

    private void onNotification(ChannelHandlerContext ctx, DBEntityProvider db,
                                GHThread thread)
    {
        final Map<String, Object> model = [:]

        final Template template

        switch (thread.type) {
            case 'Commit':
                model.commit = thread.boundCommit
                template = Template.GITHUB_COMMIT_TEMPLATE
                break
            case 'Issue':
                model.issue = thread.boundIssue
                template = Template.GITHUB_ISSUE_TEMPLATE
                break
            case 'PullRequest':
                model.pullRequest = thread.boundPullRequest
                template = Template.GITHUB_PULL_REQUEST_TEMPLATE
                break
            default:
                throw new NotImplementedException(
                        "Unable to handle $thread.type")
        }

        final access = URLEncoder.encode(db.getAuthToken(this).access, 'UTF-8')

        final commentLookup = IOUtils.toString(
                new URL("$thread.lastCommentUrl?access_token=$access"))

        final Map<String, Object> lastComment =
                new Gson().fromJson(commentLookup, HashMap)

        lastComment.body = mdToHtml(lastComment.body.toString())

        model.lastComment = lastComment
        model.thread = thread

        ctx.writeAndFlush(templateRenderer.render(template, model))
    }

    @Override
    void stop(ChannelHandlerContext ctx, DBEntityProvider db)
    {
        stop = true

        future.cancel(true)
    }

    private String mdToHtml(String md)
    {
        return new PegDownProcessor().markdownToHtml(md)
    }
}
