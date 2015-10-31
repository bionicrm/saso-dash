package io.saso.dash.config;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Singleton
public class DashConfig implements Config
{
    private static final String FILE_NAME = "config.json";

    private static final Logger logger = LogManager.getLogger();

    private final Gson gson;

    private ConfigModel model;

    @Inject
    public DashConfig(Gson gson)
    {
        this.gson = gson;
    }

    @Override
    public ConfigModel getModel()
    {
        return createModel();
    }

    /**
     * Creates the config model if necessary. Returns the already created model
     * if it exists.
     *
     * @return the created/existing config model
     */
    private synchronized ConfigModel createModel()
    {
        if (model == null) {
            try (Reader reader = new FileReader(FILE_NAME)) {
                model = gson.fromJson(reader, ConfigModel.class);
                logger.info("Read {}: {}", FILE_NAME, modelToJson());
            }
            catch (FileNotFoundException e) {
                model = new ConfigModel();
                logger.warn("{} not found, using defaults: {}", FILE_NAME,
                        modelToJson());
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return model;
    }

    /**
     * Converts the config model to a JSON string.
     *
     * @return a JSON string
     */
    private String modelToJson()
    {
        return gson.toJson(model);
    }
}
