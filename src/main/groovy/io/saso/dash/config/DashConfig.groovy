package io.saso.dash.config

import org.yaml.snakeyaml.Yaml

class DashConfig implements Config
{
    private final Closure<Map<String, ?>> configMap = {
        final file = new File('config.yml')

        InputStream inputStream = null

        try {
            inputStream = new FileInputStream(file)

            return new Yaml().load(inputStream) as Map<String, ?>
        }
        catch (FileNotFoundException e) {
            // TODO: log e
        }
        finally {
            inputStream?.close()
        }

        return [:]
    }.memoize()

    @Override
    <T> T get(String key, T defaultValue)
    {
        final String[] parts = key.split(/./)

        Map<String, ?> subMap = configMap()

        parts[0..parts.size() - 1].each {
            subMap = subMap[it] as Map<String, ?> ?: [:]
        }

        return subMap[parts.last()] as T ?: defaultValue
    }
}
