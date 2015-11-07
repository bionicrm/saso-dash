package io.saso.dash.templating.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import io.saso.dash.templating.TemplateRenderer;
import io.saso.dash.util.Resources;
import me.mazeika.uconfig.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DashTemplateRenderer implements TemplateRenderer
{
    private static final Logger logger = LogManager.getLogger();

    /**
     * The number of templates found in templates/. Not required to
     * be correct; it is simply used as the initial map size for the cache.
     */
    private static final int TEMPLATE_COUNT = 3;

    private final Map<String, String> cache =
            new ConcurrentHashMap<>(TEMPLATE_COUNT);
    private final boolean cacheTemplates;

    @Inject
    public DashTemplateRenderer(Config config)
    {
        cacheTemplates = config.getOrDefault("cache.templates", true);
    }

    @Override
    public String render(String templateName, Map<String, Object> model)
    {
        final String contents;

        if (cacheTemplates) {
            contents = cache.computeIfAbsent(templateName, this::readTemplate);
        }
        else {
            contents = readTemplate(templateName);
        }

        try {
            return new JtwigTemplate(contents, new JtwigConfiguration())
                    .output(new JtwigModelMap().add(model))
                    // strip excess whitespace
                    .replaceAll("\\s{2,}", " ");
        }
        catch (ParseException | CompileException | RenderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a Twig file's contents. E.g. if {@code template} is
     * {@code "my_template"}, then templates/my_template.twig will be read.
     *
     * @param templateName the name of the template
     *
     * @return the templates's contents
     */
    private String readTemplate(String templateName)
    {
        return Resources.get("templates/" + templateName + ".twig");
    }
}
