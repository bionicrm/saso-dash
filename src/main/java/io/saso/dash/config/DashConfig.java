package io.saso.dash.config;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.saso.dash.binding_annotations.New;
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
    private final Provider<ConfigModel> modelProvider;

    private ConfigModel model;

    @Inject
    public DashConfig(Gson gson, @New Provider<ConfigModel> modelProvider)
    {
        this.gson = gson;
        this.modelProvider = modelProvider;
    }

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
                logger.info("Read {}: {}", FILE_NAME, modelToJson(model));
            }
            catch (FileNotFoundException e) {
                model = modelProvider.get();
                logger.warn("{} not found, using defaults: {}", FILE_NAME,
                        modelToJson(model));
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Converts a ConfigModel to JSON form.
     *
     * @param model the model to convert
     *
     * @return the JSON
     */
    private String modelToJson(ConfigModel model)
    {
        return gson.toJson(model);
    }
}
