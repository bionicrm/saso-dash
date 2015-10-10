package io.saso.dash.config;

import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Singleton
public class DashConfig implements Config
{
    private static final String CONFIG_PATH = "config.yml";

    private static final Logger logger = LogManager.getLogger();

    private Optional<Map<String, Object>> map = Optional.empty();

    @Override
    public String getString(String key, String def)
    {
        initialize();

        return getValue(key, def);
    }

    @Override
    public Integer getInteger(String key, Integer def)
    {
        initialize();

        return getValue(key, def);
    }

    private <T> T getValue(String key, T def)
    {
        final List<String> keyParts = Arrays.asList(key.split("\\."));

        int lastIndex = keyParts.size() - 1;
        Map subMap = map.get();

        for (String part : keyParts.subList(0, lastIndex)) {
            subMap = ((Map) subMap.get(part));
        }

        // noinspection unchecked
        return (T) subMap.getOrDefault(keyParts.get(lastIndex), def);
    }

    private synchronized void initialize()
    {
        if (map.isPresent()) return;

        final Yaml yaml = new Yaml();
        final FileInputStream in;

        try {
            in = new FileInputStream(CONFIG_PATH);
        }
        catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return;
        }

        final Object o = yaml.load(in);

        // noinspection unchecked
        map = Optional.of((Map<String, Object>) o);

        purgeMapOfNulls(map.get());

        try {
            logger.info("Loaded config @ {} => {}",
                    new File(CONFIG_PATH).getCanonicalPath(), map.get());
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void purgeMapOfNulls(Map<String, Object> map)
    {
        final Iterator<Map.Entry<String, Object>> itr =
                map.entrySet().iterator();

        while (itr.hasNext()) {
            final Map.Entry<String, Object> entry = itr.next();
            final Object value = entry.getValue();

            if (value instanceof Map) {
                // noinspection unchecked
                purgeMapOfNulls((Map<String, Object>) value);
            }
            else if (value == null) {
                itr.remove();
            }
        }
    }
}
