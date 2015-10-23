package io.saso.dash.templating

interface TemplateRenderer
{
    String render(Template template, Map<String, Object> model)
}
