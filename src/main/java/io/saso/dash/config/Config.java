package io.saso.dash.config;

public interface Config
{
    String getString(String key, String def);

    Integer getInteger(String key, Integer def);
}
