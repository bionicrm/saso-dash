package io.saso.dash.config

public interface ConfigOLD
{
    /**
     * Gets a value from the configuration. [key] is in dot notation. The type
     * of [def] determines the returned type ([T]). [def] defines the
     * value to return if the specified key does not exist or the key exists but
     * there is a `null` value.
     *
     * @param key the key to look up
     * @param def the default value to return
     *
     * @return the value located at [key]
     */
    fun <T> get(key: String, def: T): T
}
