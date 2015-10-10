package io.saso.dash.config;

import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Singleton
public class DashConfig implements Config
{
    private static final String CONFIG_PATH = "config.yml";

    private static final Logger logger = LogManager.getLogger();

    private Map<String, Object> map;

    @Override
    public <T> T get(String key, T def)
    {
        initialize();

        final List<String> keyParts = Arrays.asList(key.split("\\."));
        final int lastIndex = keyParts.size() - 1;

        Map subMap = map;

        for (String part : keyParts.subList(0, lastIndex)) {
            subMap = ((Map) subMap.get(part));
        }

        // noinspection unchecked
        return (T) subMap.getOrDefault(keyParts.get(lastIndex), def);
    }

    private synchronized void initialize()
    {
        if (map != null) return;

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
        map = (Map<String, Object>) o;

        purgeMapOfNulls(map);

        try {
            logger.info("Loaded config @ {} => {}",
                    new File(CONFIG_PATH).getCanonicalPath(), map);
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
