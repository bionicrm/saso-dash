package io.saso.dash.templating

import com.lyncode.jtwig.JtwigModelMap

public interface Templater
{
    /**
     * Renders a template using the provided model.
     *
     * @param template the name of the template
     * @param modelMap the model to pass to the template
     *
     * @return the rendered template
     */
    fun render(template: String, modelMap: JtwigModelMap): String
}
