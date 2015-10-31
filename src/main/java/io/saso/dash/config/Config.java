package io.saso.dash.config;

import java.util.Optional;

public interface Config
{
    /**
     * Gets a value from the config. The key is in dot notation, e.g.
     * {@code "path.to.value"}.
     *
     * @param key the value's key
     * @param <T> the desired type of the value
     *
     * @return an optional of the value; empty when the value is {@code null} or
     *         not found
     */
    <T> Optional<T> get(String key);
}
