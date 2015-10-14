package io.saso.dash.services.github;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.lyncode.jtwig.JtwigModelMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.saso.dash.services.DBEntityProvider;
import io.saso.dash.services.Service;
import io.saso.dash.services.SubServiceAdapter;
import io.saso.dash.templating.Templater;
import io.saso.dash.util.LoggingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.kohsuke.github.*;

import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class GitHubNotificationSubService extends SubServiceAdapter
{
    private final Templater templater;
    private final Service superService;
    private final GitHub gitHub;

    @Inject
    public GitHubNotificationSubService(Templater templater,
                                        @Assisted Service superService,
                                        @Assisted GitHub github)
    {
        this.templater = templater;
        this.superService = superService;
        this.gitHub = github;
    }

    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        poll(ctx, db);
    }

    @Override
    public void poll(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        final long startPoll = System.nanoTime();

        final GHNotificationStream notifs = gitHub.listNotifications();

        notifs.read(true);
        notifs.nonBlocking(true);

        for (GHThread thread : notifs) {
            final JtwigModelMap modelMap = new JtwigModelMap();

            modelMap.add("repo", thread.getRepository().getFullName());
            modelMap.add("thread_title", thread.getTitle());

            final String type = thread.getType();
            final String lastCommentUrl = thread.getLastCommentUrl();
            final int lastCommentId = Integer.parseInt(
                    lastCommentUrl.substring(
                            lastCommentUrl.lastIndexOf('/') + 1));

            modelMap.add("type", type);

            try {
                if (type.equals("Issue")) {
                    final GHIssue issue = thread.getBoundIssue();

                    modelMap.add("thread_id", issue.getNumber());

                    for (GHIssueComment comment : issue.listComments()) {
                        if (comment.getId() == lastCommentId) {
                            modelMap.add("actor", comment.getUser().getLogin());
                            modelMap.add("actor_avatar_url", comment.getUser()
                                    .getAvatarUrl());
                            modelMap.add("comment", comment.getBody());
                            modelMap.add("time", comment.getCreatedAt()
                                    .getTime());

                            // lookup comment html url
                            final String url = comment.getUrl().toString()
                                    + "?access_token="
                                    + URLEncoder.encode(
                                        db.authToken(superService).getAccess(),
                                    "UTF-8");

                            modelMap.add("url", new Gson().fromJson(
                                    IOUtils.toString(new URL(url)),
                                    CommentJSON.class).html_url);
                        }
                    }
                }
                else if (type.equals("Commit")) {
                    final GHCommit commit = thread.getBoundCommit();

                    for (GHCommitComment comment : commit.listComments()) {
                        if (comment.getId() == lastCommentId) {
                            modelMap.add("actor", comment.getUser().getLogin());
                            modelMap.add("actor_avatar_url", comment.getUser()
                                    .getAvatarUrl());
                            modelMap.add("comment", comment.getBody());
                            modelMap.add("time", comment.getCreatedAt()
                                    .getTime());
                            modelMap.add("url",
                                    comment.getHtmlUrl().toString());
                        }
                    }
                }
            }
            catch (Exception e) {
                LoggingUtil.logThrowable(e, getClass());
            }

            ctx.channel().write(new TextWebSocketFrame(
                    templater.render("templates/github/comment.twig",
                            modelMap)));
        }

        final long endPoll = System.nanoTime();

        LogManager.getLogger().trace(
                "Took {}ms to complete GitHub notification poll",
                TimeUnit.MILLISECONDS.convert(endPoll - startPoll,
                        TimeUnit.NANOSECONDS));
    }

    private class CommentJSON
    {
        String html_url;
    }
}
