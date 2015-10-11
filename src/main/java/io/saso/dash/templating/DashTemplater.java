package io.saso.dash.templating;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import io.saso.dash.config.Config;
import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DashTemplater implements Templater
{
    private final boolean cacheTemplates;
    private final Map<String, String> templates = new HashMap<>();
    private final JtwigConfiguration jConfig = new JtwigConfiguration();

    @Inject
    public DashTemplater(Config config)
    {
        cacheTemplates = config.get("cache-templates", false);
    }

    @Override
    public String render(String template, JtwigModelMap modelMap)
            throws IOException, ParseException, CompileException,
            RenderException
    {
        String contents = templates.get(template);

        if (contents == null) {
            contents = IOUtils.toString(new FileReader(template));

            if (cacheTemplates) {
                templates.put(template, contents);
            }
        }

        return new JtwigTemplate(contents, jConfig).output(modelMap);
    }
}
