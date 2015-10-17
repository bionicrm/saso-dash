package io.saso.dash.config;

import com.google.inject.Singleton;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DashConfigOLD implements Config
{
    private static final String CONFIG_PATH = "config.yml";
    private static final Logger LOGGER = LogManager.getLogger();

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
        final T value = (T) subMap.get(keyParts.get(lastIndex));

        return value == null ? def : value;
    }

    /**
     * Initializes this configuration by reading and parsing the YAML into a
     * map.
     */
    private synchronized void initialize()
    {
        // TODO: copy a default config if none exists?

        if (map != null) return;

        final Yaml yaml = new Yaml();
        final FileInputStream in;

        try {
            in = new FileInputStream(CONFIG_PATH);
        }
        catch (FileNotFoundException e) {
            LOGGER.warn("No {} found, using defaults", CONFIG_PATH);

            map = new HashMap<>();

            return;
        }

        final Object o = yaml.load(in);

        // noinspection unchecked
        map = (Map<String, Object>) o;

        try {
            LOGGER.info("Loaded config @ {} => {}",
                    new File(CONFIG_PATH).getCanonicalPath(), map);
        }
        catch (IOException e) {
            LoggingUtil.logThrowable(e, getClass());
        }
    }
}
