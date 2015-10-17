package io.saso.dash.templating

import com.google.inject.Inject
import com.google.inject.Singleton
import com.lyncode.jtwig.JtwigModelMap
import com.lyncode.jtwig.JtwigTemplate
import com.lyncode.jtwig.configuration.JtwigConfiguration
import io.saso.dash.config.Config
import io.saso.dash.util.Resources
import org.apache.commons.io.IOUtils
import java.io.FileReader
import java.util.*

@Singleton
public class DashTemplater
@Inject
constructor(val config: Config) : Templater
{
    private val trimSpacesRegex = "\\s{2,}".toRegex()
    private val cacheTemplates = config.get("cache-templates", false)
    private val templates: MutableMap<String, String> = HashMap()

    override fun render(template: String, modelMap: JtwigModelMap): String
    {
        val contents = if (cacheTemplates) {
            templates.getOrPut(template, {
                this getTemplate template
            })
        } else {
            this getTemplate template
        }

        return JtwigTemplate(contents, JtwigConfiguration())
                .output(modelMap)
                // replace 2+ spaces with 1
                .replace(trimSpacesRegex, " ")
    }

    private fun getTemplate(template: String) =
            Resources get "templates/$template.twig"

}
