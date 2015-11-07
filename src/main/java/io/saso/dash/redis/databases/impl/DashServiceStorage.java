package io.saso.dash.redis.databases.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.RedisDatabase;
import io.saso.dash.redis.databases.ServiceStorage;
import io.saso.dash.services.Service;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class DashServiceStorage implements ServiceStorage
{
    /**
     * GSON is used for serialization. It allows the output to be compacted
     * JSON.
     */
    private static final Gson gson = new Gson();

    /**
     * YAML is used for deserialization. This is because numbers are doubles or
     * integers, rather than solely doubles like for JSON.
     */
    private static final Yaml yaml = new Yaml();

    private final Redis redis;

    @Inject
    public DashServiceStorage(Redis redis)
    {
        this.redis = redis;
    }

    @Override
    public void pushModel(Service service, int userId,
                          Map<String, Object> model)
    {
        final String json = gson.toJson(model);
        final String key = getKey(service, userId);

        try (Jedis connection = redis.getConnection(RedisDatabase.SERVICES)) {
            connection.rpush(key, json);
        }
    }

    @Override
    public synchronized Stream<Map<String, Object>> popModels(Service service,
                                                              int userId)
    {
        final String key = getKey(service, userId);
        final List<String> models;

        try (Jedis connection = redis.getConnection(RedisDatabase.SERVICES)) {
            final Pipeline p = connection.pipelined();
            final Response<List<String>> list = p.lrange(key, 0, -1);

            p.ltrim(key, 1, 0);
            p.sync();

            models = list.get();
        }

        // noinspection unchecked
        return models.stream().map(model ->
                (Map<String, Object>) yaml.load(model));
    }

    private String getKey(Service service, int userId)
    {
        return service.getName() + userId;
    }
}
