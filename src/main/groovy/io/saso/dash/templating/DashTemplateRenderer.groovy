package io.saso.dash.templating
import com.google.inject.Inject
import com.lyncode.jtwig.JtwigModelMap
import com.lyncode.jtwig.JtwigTemplate
import com.lyncode.jtwig.configuration.JtwigConfiguration
import io.saso.dash.config.Config

class DashTemplateRenderer implements TemplateRenderer
{
    private final boolean cacheTemplates
    private final Map<Template, String> templates = new HashMap()

    @Inject
    DashTemplateRenderer(Config config)
    {
        cacheTemplates = config.get 'cache-templates', false
    }

    @Override
    String render(Template template, Map<String, Object> model)
    {
        def final contents = {
            if (cacheTemplates) {
                templates.putIfAbsent template, template.fetch()
            }
            else {
                template.fetch()
            }
        }

        def final jtwigModel = new JtwigModelMap()

        jtwigModel.add(model)

        return new JtwigTemplate(contents(), new JtwigConfiguration())
                .output(jtwigModel)
                // replace >=2 spaces with 1 space
                .replaceAll(~/\s{2,}/, ' ')
    }


}
