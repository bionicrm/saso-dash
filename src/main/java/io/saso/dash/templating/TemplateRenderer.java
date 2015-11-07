package io.saso.dash.templating;

import java.util.Map;

public interface TemplateRenderer
{
    /**
     * Renders the given Twig template using the given model.
     * {@code templateName} should be a path relative to ./templates that
     * excludes the file extension. E.g. {@code "path/to/file"} would render
     * {@code "./templates/path/to/file.twig"}.
     * <p>
     * If configured to do so, the templates will be cached. The application
     * must be restarted for the cache to refresh. If cache is disabled, the
     * files will be read every time it's rendered.
     *
     * @param templateName the name of the template to load
     * @param model the model to pass to the Twig template
     *
     * @return the rendered Twig template
     */
    String render(String templateName, Map<String, Object> model);
}
