package io.saso.dash.services.github

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.lyncode.jtwig.JtwigModelMap
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.services.DBEntityProvider
import io.saso.dash.services.SubServiceAdapter
import io.saso.dash.templating.Templater
import io.saso.dash.util.logger
import org.kohsuke.github.GHEvent
import org.kohsuke.github.GHEventPayload
import org.kohsuke.github.GitHub
import org.pegdown.PegDownProcessor
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("NAME_SHADOWING")
public class GitHubNotificationSubService
@Inject
constructor(private val templater: Templater,
            @Assisted private val gitHub: GitHub) : SubServiceAdapter()
{
    // TODO: tmp
    private val usedEvents: MutableList<Int> = ArrayList()

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider) =
            poll(ctx, db)

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        val startPoll = System.nanoTime()

        logger(this) debug "Rate limit update: " +
                "limit=${gitHub.rateLimit.limit} " +
                "left=${gitHub.rateLimit.remaining}"

        logger(this) debug "Looking for events..."

        val page = gitHub.myself.listEvents().iterator()

        if (page.hasNext()) {
            page.nextPage().forEach { event ->
                if (event.createdAt.toInstant().isBefore(
                        Instant.now().minusSeconds(60 * 60))) {
                    return
                }

                logger(this) debug "Event ${event.type}"

                val model = JtwigModelMap()

                if (event.type == GHEvent.ISSUE_COMMENT) {
                    val specificEvent = event.getPayload(
                            GHEventPayload.IssueComment::class.java)

                    if (usedEvents contains specificEvent.comment.id) {
                        return
                    } else {
                        usedEvents add specificEvent.comment.id
                    }

                    model.add("repo", event.repository)
                    model.add("user", event.actor)
                    model.add("comment", specificEvent.comment)
                    model.add("issue", specificEvent.issue)

                    write(ctx, templater.render("github/issue_comment", model))
                }
            }
        }

        val ms = TimeUnit.MILLISECONDS.convert(
                System.nanoTime() - startPoll, TimeUnit.NANOSECONDS)

        logger(this) trace
                "Took ${ms}ms to complete GitHub notification poll"
    }

    private fun mdToHtml(md: String): String =
            PegDownProcessor().markdownToHtml(md)
}
