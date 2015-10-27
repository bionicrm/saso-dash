package io.saso.dash.config;

public interface Config
{
    /**
     * Gets the config model that holds information about the config. Creates a
     * new one if it does not yet exist.
     *
     * @return the config model
     */
    ConfigModel getModel();
}
