package io.saso.dash.services.github

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.lyncode.jtwig.JtwigModelMap
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.Service
import io.saso.dash.services.SubServiceAdapter
import io.saso.dash.templating.Templater
import io.saso.dash.util.logThrowable
import io.saso.dash.util.logger
import org.apache.commons.io.IOUtils
import org.kohsuke.github.GitHub
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

public class GitHubNotificationSubService
@Inject
constructor(private val templater: Templater,
            @Assisted private val superService: Service,
            @Assisted private val gitHub: GitHub) : SubServiceAdapter()
{
    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        poll(ctx, db)
    }

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        val startPoll = System.nanoTime()

        val notifs = gitHub.listNotifications()

        notifs.read(true)
        notifs.nonBlocking(true)

        for (thread in notifs) {
            val modelMap = JtwigModelMap()

            modelMap.add("repo", thread.repository.fullName)
            modelMap.add("thread_title", thread.title)

            val type = thread.type
            val lastCommentUrl = thread.lastCommentUrl
            val lastCommentId = Integer.parseInt(
                    lastCommentUrl.substring(
                            lastCommentUrl.lastIndexOf('/') + 1))

            modelMap.add("type", type)

            try {
                if (type == "Issue") {
                    val issue = thread.boundIssue

                    modelMap.add("thread_id", issue.number)

                    for (comment in issue.listComments()) {
                        if (comment.id == lastCommentId) {
                            modelMap.add("actor", comment.user.login)
                            modelMap.add("actor_avatar_url",
                                    comment.user.avatarUrl)
                            modelMap.add("comment", comment.body)
                            modelMap.add("time", comment.createdAt.time)

                            // lookup comment html url
                            val url = "${comment.url}?access_token=" +
                                    "${URLEncoder.encode(
                                            db.authToken(superService).access,
                                            "UTF-8")}"

                            modelMap.add("url", Gson().fromJson(
                                    IOUtils.toString(URL(url)),
                                    CommentJSON::class.java).html_url)
                        }
                    }
                } else if (type == "Commit") {
                    val commit = thread.boundCommit

                    for (comment in commit.listComments()) {
                        if (comment.id == lastCommentId) {
                            modelMap.add("actor", comment.user.login)
                            modelMap.add("actor_avatar_url",
                                    comment.user.avatarUrl)
                            modelMap.add("comment", comment.body)
                            modelMap.add("time", comment.createdAt.time)
                            modelMap.add("url",
                                    comment.htmlUrl.toString())
                        }
                    }
                }
            } catch (e: Throwable) {
                logThrowable(this, e)
            }

            ctx.channel().write(TextWebSocketFrame(
                    templater.render("github/comment",
                            modelMap)))
        }

        val endPoll = System.nanoTime()
        val ms = TimeUnit.MILLISECONDS.convert(
                endPoll - startPoll, TimeUnit.NANOSECONDS)

        logger(this) trace "Took $ms ms to complete GitHub notification poll"

    }

    private inner class CommentJSON
    {
        internal var html_url: String = ""
    }
}
