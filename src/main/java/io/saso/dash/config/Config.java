package io.saso.dash.config;

public interface Config
{
    String getString(String key, String def);

    default String getString(String key)
    {
        return getString(key, null);
    }

    Integer getInteger(String key, Integer def);

    default Integer getInteger(String key)
    {
        return getInteger(key, null);
    }
}
