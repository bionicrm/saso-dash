package io.saso.dash.services.github;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lyncode.jtwig.JtwigModelMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;
import io.saso.dash.redis.Redis;
import io.saso.dash.services.Service;
import io.saso.dash.templating.Templater;
import io.saso.dash.util.LoggingUtil;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.*;

import java.net.URL;
import java.net.URLEncoder;

public class GitHubService implements Service
{
    private static final Gson GSON = new Gson();

    private final Redis redis;
    private final Templater templater;

    private ChannelHandlerContext ctx;
    private User user;
    private ProviderUser providerUser;
    private AuthToken authToken;

    private GitHub gitHub;

    @Inject
    public GitHubService(Redis redis, Templater templater)
    {
        this.redis = redis;
        this.templater = templater;
    }

    @Override
    public void start() throws Exception
    {
        gitHub = GitHub.connectUsingOAuth(authToken.getAccess());
    }

    @Override
    public void poll() throws Exception
    {
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
                                    + URLEncoder.encode(authToken.getAccess(),
                                    "UTF-8");

                            modelMap.add("url", GSON.fromJson(
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

        ctx.channel().flush();

        // TODO
    }

    @Override
    public void stop() throws Exception
    {

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

    @Override
    public void setContext(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public void setProviderUser(ProviderUser providerUser)
    {
        this.providerUser = providerUser;
    }

    @Override
    public void setAuthToken(AuthToken authToken)
    {
        this.authToken = authToken;
    }

    private class CommentJSON
    {
        String html_url;
    }
}
