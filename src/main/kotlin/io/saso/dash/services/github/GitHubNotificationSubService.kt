package io.saso.dash.services.github

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.lyncode.jtwig.JtwigModelMap
import io.netty.channel.ChannelHandlerContext
import io.saso.dash.redis.databases.RedisServices
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.services.SubServiceAdapter
import io.saso.dash.templating.TemplateRenderer
import io.saso.dash.util.THREAD_POOL
import io.saso.dash.util.logger
import org.apache.commons.io.IOUtils
import org.kohsuke.github.GHThread
import org.kohsuke.github.GitHub
import org.pegdown.PegDownProcessor
import java.net.URL
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.Future
import kotlin.properties.Delegates

@Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
public class GitHubNotificationSubServiceOLD
@Inject
constructor(private val templater: TemplateRenderer,
            private val redisServices: RedisServices,
            @Assisted private val gitHub: GitHub) : SubServiceAdapter()
{
    private var stopping = false;
    private var future: Future<*> by Delegates.notNull()

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        val itr = gitHub.listNotifications()
                .since(System.currentTimeMillis() - 60 * 60)
                .read(true)
                .iterator()

        val threadsFromRedis =
                redisServices.get(db.liveToken().userId, "github")

        if (threadsFromRedis.isNotEmpty()) {
            val threadsJson = Gson().fromJson(threadsFromRedis,
                    HashMap::class.java)

            threadsJson.forEach {
                val threadJson =
                        Gson().fromJson(it.value.toString(),
                                HashMap::class.java)
                val model = JtwigModelMap()

                threadJson.forEach {
                    model.put(it.key.toString(), it.value.toString())
                }

                val rendered =
                        templater.render(model.get("template").toString(),
                                model)

                write(ctx, rendered)
            }
        }

        future = THREAD_POOL submit {
            while (! stopping) {
                logger(this@GitHubNotificationSubService) debug
                        "onNotification waiting..."
                onNotification(ctx, db, itr.next())
            }
        }
    }

    fun onNotification(ctx: ChannelHandlerContext, db: DBEntityProvider,
                       thread: GHThread)
    {
        val model = JtwigModelMap()

        val template = "github/" + when (thread.type) {
            "Commit" -> {
                model.add("commit", thread.boundCommit)
                "commit"
            }
            "Issue" -> {
                model.add("issue", thread.boundIssue)
                "issue"
            }
            "PullRequest" -> {
                model.add("pullRequest", thread.boundPullRequest)
                "pull_request"
            }
            else -> return
        }

        model.add("template", template)

        val access = "?access_token=" +
                URLDecoder.decode(db.authToken("github").access, "UTF-8")
        val commentLookup =
                IOUtils.toString(URL(thread.lastCommentUrl + access))
        val lastComment: HashMap<Any, Any> =
                Gson().fromJson(commentLookup, HashMap::class.java)
                        as HashMap<Any, Any>

        // Markdown to HTML
        lastComment.set("body", mdToHtml(lastComment.get("body").toString()))

        model.add("lastComment", lastComment)
        model.add("thread", thread)

        redisServices.set(db.liveToken().userId, "github", { data ->
            val currentJson = Gson().fromJson(data, HashMap::class.java)
                    as HashMap<Any, Any>
            val threadJson = Gson() toJson model

            if (currentJson.putIfAbsent(thread.id, threadJson) == null) {
                val rendered = templater.render(template, model)

                write(ctx, rendered)
            }

            Gson() toJson currentJson
        })
    }

    override fun stop(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        stopping = true

        future cancel true
    }

    private fun mdToHtml(md: String): String =
            PegDownProcessor().markdownToHtml(md)
}
