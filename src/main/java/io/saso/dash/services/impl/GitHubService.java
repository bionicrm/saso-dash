package io.saso.dash.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import io.saso.dash.server.Client;
import io.saso.dash.services.Service;
import io.saso.dash.templating.TemplateRenderer;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHNotificationStream;
import org.kohsuke.github.GHThread;
import org.kohsuke.github.GitHub;
import org.pegdown.PegDownProcessor;

import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GitHubService implements Service
{
    private static final Gson gson = new Gson();

    private final TemplateRenderer templateRenderer;

    private GitHub gitHub;

    @Inject
    public GitHubService(TemplateRenderer templateRenderer)
    {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public String getName()
    {
        return "github";
    }

    @Override
    public int getPollInterval()
    {
        return 30;
    }

    @Override
    public void start(Client client) throws Exception
    {
        gitHub = GitHub.connectUsingOAuth(client.authToken(this).getAccess());

        poll(client);
    }

    @Override
    public void poll(Client client) throws Exception
    {
        // TODO: we don't want read messages
        final GHNotificationStream stream = gitHub.listNotifications()
                .nonBlocking(true).read(true);

        for (GHThread thread : stream) {
            final Map<String, Object> model = new HashMap<>();

            String template;

            switch (thread.getType()) {
                case "Commit":
                    model.put("commit", thread.getBoundCommit());
                    template = "github/commit";
                    break;
                case "Issue":
                    model.put("issue", thread.getBoundIssue());
                    template = "github/issue";
                    break;
                case "PullRequest":
                    model.put("pullRequest", thread.getBoundPullRequest());
                    template = "github/pull_request";
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Unknown thread type");
            }

            final String access = URLEncoder.encode(
                    client.authToken(this).getAccess(), "UTF-8");

            final String commentLookup = IOUtils.toString(new URL(
                    thread.getLastCommentUrl() + "?access_token=" + access));

            final Type type = new TypeToken<Map<String, Object>>() {}.getType();

            final Map<String, Object> lastComment =
                    gson.fromJson(commentLookup, type);

            lastComment.put("body",
                    mdToHtml(lastComment.get("body").toString()));

            model.put("lastComment", lastComment);
            model.put("thread", thread);

            client.write(templateRenderer.render(template, model)).flush();
        }
    }

    private String mdToHtml(String md)
    {
        return new PegDownProcessor().markdownToHtml(md);
    }
}
