package io.saso.dash.config.impl;

import com.google.inject.Singleton;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class DashConfig implements Config
{
    private static final String FILE_NAME = "config.yaml";

    private static final Logger logger = LogManager.getLogger();
    private static final Yaml yaml = new Yaml();

    private Map<String, Object> model;

    @Override
    public <T> Optional<T> get(String key)
    {
        Map<String, Object> model = getModel();
        String[] parts = key.split("\\.");

        for (int i = 0; i < parts.length - 1; i++)
        {
            Object value = model.get(parts[i]);

            if (value == null || ! (value instanceof Map)) {
                return Optional.empty();
            }

            // noinspection unchecked
            model = (Map<String, Object>) value;
        }

        // noinspection unchecked
        T value = (T) model.get(parts[parts.length - 1]);

        return Optional.ofNullable(value);
    }

    /**
     * Gets the config model, creating it if necessary.
     *
     * @return the created/existing config model
     */
    private synchronized Map<String, Object> getModel()
    {
        if (model == null) {
            try (Reader reader = new FileReader(FILE_NAME)) {
                // noinspection unchecked
                model = (Map<String, Object>) yaml.load(reader);
                logger.info("Read {}: {}", FILE_NAME, model);
            }
            catch (FileNotFoundException e) {
                model = new HashMap<>();
                logger.warn("{} not found, using defaults", FILE_NAME);
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return model;
    }
}
