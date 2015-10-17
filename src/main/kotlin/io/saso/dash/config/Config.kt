package io.saso.dash.config

public interface Config
{
    /**
     * Gets a value from the configuration. [key] is in dot notation. The type
     * of [default] determines the returned type ([T]). [default] defines the
     * value to return if the specified key does not exist or the key exists but
     * there is a `null` value.
     *
     * @param key the key to look up
     * @param default the default value to return
     *
     * @return the value located at [key]
     */
    fun <T> get(key: String, default: T): T
}
