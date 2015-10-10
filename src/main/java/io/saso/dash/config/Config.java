package io.saso.dash.config;

/**
 * Represents a configuration file. Loads and read a YAML file at
 * {@code ./config.yml}.
 */
public interface Config
{
    /**
     * Gets a value from the configuration. The type returned is defined by the
     * type of {@code def}. The key is in dot notation. Example:
     * <pre>
     *     // config.yml:
     *     path:
     *       to:
     *         value: hello world
     *
     *     // some .java file:
     *     config.get("path.to.value", "default"); // returns "hello world"
     * </pre>
     *
     * @param key the path to the value in dot notation
     * @param def the default value; also determines the type if it is found
     * @param <T> the type of value located at the path specified by {@code key}
     *
     * @return the value at the specified path, casted to {@code <T>}
     */
    <T> T get(String key, T def);
}
