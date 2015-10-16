package io.dash.templating

import com.google.inject.Inject
import com.google.inject.Singleton
import com.lyncode.jtwig.JtwigModelMap
import com.lyncode.jtwig.JtwigTemplate
import com.lyncode.jtwig.configuration.JtwigConfiguration
import io.saso.dash.config.Config
import org.apache.commons.io.IOUtils
import java.io.FileReader
import java.util.*

@Singleton
public class DashTemplater @Inject constructor(val config: Config) : Templater
{
    private val cacheTemplates = config.get("cache-templates", false)
    private val templates: MutableMap<String, String> = HashMap()
    private val jConfig = JtwigConfiguration()

    override fun render(template: String, modelMap: JtwigModelMap): String
    {
        val contents = if (cacheTemplates) {
            templates.getOrPut(template, {
                getTemplateContents(template)
            })
        } else {
            getTemplateContents(template)
        }

        val output = JtwigTemplate(contents, jConfig)
                .output(modelMap)
                // replace 2+ spaces with 1
                .replace("\\s{2,}", " ")

        return output
    }

    private fun getTemplateContents(template: String): String
    {
        return IOUtils.toString(FileReader(template))
    }

}
