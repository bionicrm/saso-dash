package io.saso.dash.config

interface Config
{
    def <T> T get(String key, T defaultValue)
}
