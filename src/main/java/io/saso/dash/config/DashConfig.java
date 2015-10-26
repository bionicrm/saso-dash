package io.saso.dash.config;

import com.google.gson.Gson;
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
    private static final Gson gson = new Gson();

    private ConfigModel model;

    @Override
    public ConfigModel getModel()
    {
        readConfig();
        return model;
    }

    /**
     * Reads the config file if necessary. Sets {@link #model}. If no config
     * file is found, an empty model will be used.
     */
    private synchronized void readConfig()
    {
        if (model == null) {
            try (Reader reader = new FileReader(FILE_NAME)) {
                model = gson.fromJson(reader, ConfigModel.class);
                logger.info("Read {}: {}", FILE_NAME, gson.toJson(model));
            }
            catch (FileNotFoundException e) {
                model = new ConfigModel();
                logger.warn("{} not found, using defaults: {}", FILE_NAME,
                        gson.toJson(model));
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
