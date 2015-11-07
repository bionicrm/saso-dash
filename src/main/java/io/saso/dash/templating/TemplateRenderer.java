package io.saso.dash.templating;

import java.util.Map;

public interface TemplateRenderer
{
    String render(String templateName, Map<String, Object> model);
}
