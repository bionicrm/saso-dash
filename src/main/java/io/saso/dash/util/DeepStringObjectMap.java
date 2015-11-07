package io.saso.dash.util;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public final class DeepStringObjectMap<V>
{
    private final Map<String, Object> map;

    private DeepStringObjectMap(Map<String, Object> map)
    {
        this.map = map;
    }

    public static <V> DeepStringObjectMap<V> from(Map<String, Object> map)
    {
        return new DeepStringObjectMap<>(map);
    }

    public static <V> DeepStringObjectMap<V> from(Object map)
    {
        // noinspection unchecked
        return from((Map<String, Object>) map);
    }

    public V get(String... keys)
    {
        Map<String, Object> map = this.map;

        for (String key : keys)
        {
            Object obj = map.get(key);

            if (obj instanceof Map) {
                // noinspection unchecked
                map = (Map<String, Object>) map.get(key);
            }
            else {
                //noinspection unchecked
                return (V) obj;
            }
        }

        throw new NoSuchElementException("Couldn't find value for given keys: "
                + Arrays.toString(keys));
    }
}
