package io.saso.config

import com.google.inject.Singleton
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream

@Singleton @Suppress("UNCHECKED_CAST")
public class DashConfig : Config
{
    private val map: Map<String, Any> by lazy {
        Yaml().load(FileInputStream("config.yml")) as Map<String, Any>
    }

    override fun <T> get(key: String, default: T): T
    {
        val parts = key.split('.')

        var subMap = map;

        parts.subList(0, parts.lastIndex).forEachIndexed { i, s ->
            subMap = subMap.get(s) as Map<String, Any>
        }

        // can't use Map#getOrDefault(); if the *value* is `null`, then we'd
        // return `null`, which we don't want; we want to return [default]
        return subMap.get(parts.last()) as T ?: default
    }
}
