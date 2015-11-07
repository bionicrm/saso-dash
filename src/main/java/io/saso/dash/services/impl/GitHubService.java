package io.saso.dash.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import io.saso.dash.redis.databases.ServiceStorage;
import io.saso.dash.server.Client;
import io.saso.dash.services.Service;
import io.saso.dash.templating.TemplateRenderer;
import io.saso.dash.util.DeepStringObjectMap;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.*;

import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static io.saso.dash.util.DeepStringObjectMap.from;

public class GitHubService implements Service
{
    private static final Gson gson = new Gson();

    private final TemplateRenderer templateRenderer;
    private final ServiceStorage storage;
    private final Set<Integer> usedIdSet = new HashSet<>();

    private GitHub gitHub;

    @Inject
    public GitHubService(TemplateRenderer templateRenderer,
                         ServiceStorage storage)
    {
        this.templateRenderer = templateRenderer;
        this.storage = storage;
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

        readFromStorage(client);
        client.flush();

        poll(client);
    }

    @Override
    public void poll(Client client) throws Exception
    {
        // TODO: we don't want read messages?
        final GHNotificationStream stream = gitHub.listNotifications()
                .nonBlocking(true).read(true);

        for (GHThread thread : stream) {
            final Map<String, Object> model = new HashMap<>();

            String template;

            switch (thread.getType()) {
                case "Commit":
                    final GHCommit commit = thread.getBoundCommit();
                    //model.put("commit", thread.getBoundCommit());
                    template = "github/commit";
                    break;
                case "Issue":
                    final GHIssue issue = thread.getBoundIssue();
                    model.put("issue_number", issue.getNumber());
                    model.put("issue_title", issue.getTitle());
                    model.put("created_at", issue.getCreatedAt());
                    template = "github/issue";
                    break;
                case "PullRequest":
                    final GHPullRequest pr = thread.getBoundPullRequest();
                    //model.put("pullRequest", thread.getBoundPullRequest());
                    model.put("created_at", pr.getCreatedAt());
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

            lastComment.put("body", IOUtils.toString(thread.getRepository()
                    .renderMarkdown(lastComment.get("body").toString(),
                            MarkdownMode.GFM)));

            model.putIfAbsent("created_at", lastComment.get("created_at"));
            model.compute("created_at", (k, v) -> ((Date) v).getTime());

            final DeepStringObjectMap lastCommentDeep = from(lastComment);

            model.put("last_comment_user_avatar_url",
                    lastCommentDeep.get("user", "avatar_url"));
            model.put("last_comment_user_url",
                    lastCommentDeep.get("user", "html_url"));
            model.put("last_comment_user_login", lastCommentDeep
                    .get("user", "login"));
            model.put("last_comment_body", lastComment.get("body"));
            model.put("repo_url", thread.getRepository().getHtmlUrl()
                    .toString());
            model.put("repo_name", thread.getRepository().getFullName());
            model.put("template", template);
            model.put("id", thread.getId());

            storage.pushModel(this, client, model);
            writeModel(client, template, model);
        }

        client.flush();
    }

    /**
     * Writes the template with the given model out to the client.
     *
     * @param client the client to send the template to
     * @param template the template to render
     * @param model the model to pass to the template
     */
    private synchronized void writeModel(Client client, String template,
                            Map<String, Object> model)
    {
        int id = (int) model.get("id");

        if (! usedIdSet.contains(id)) {
            usedIdSet.add(id);
            client.write(templateRenderer.render(template, model));
        }
    }

    /**
     * Reads the models from storage and writes them to the client, not
     * flushing.
     *
     * @param client the client to read data for and to send the templates to
     */
    private void readFromStorage(Client client)
    {
        storage.peekModels(this, client).forEach(model ->
                writeModel(client, model.get("template").toString(), model));
    }
}
