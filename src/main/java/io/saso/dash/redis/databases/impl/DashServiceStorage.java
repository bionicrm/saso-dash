package io.saso.dash.redis.databases.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.RedisDatabase;
import io.saso.dash.redis.databases.ServiceStorage;
import io.saso.dash.server.Client;
import io.saso.dash.services.Service;
import me.mazeika.uconfig.Config;
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
    private final Config config;

    @Inject
    public DashServiceStorage(Redis redis, Config config)
    {
        this.redis = redis;
        this.config = config;
    }

    @Override
    public void initialize()
    {
        if (config.getOrDefault("debug", false)) {
            try (Jedis connection = redis.getConnection(
                    RedisDatabase.SERVICES)) {
                connection.flushDB();
            }
        }
    }

    @Override
    public void pushModel(Service service, Client client,
                          Map<String, Object> model)
    {
        final String json = gson.toJson(model);
        final String key = getKey(service, client);

        try (Jedis connection = redis.getConnection(RedisDatabase.SERVICES)) {
            connection.rpush(key, json);
        }
    }

    @Override
    public synchronized Stream<Map<String, Object>> peekModels(Service service,
                                                               Client client)
    {
        final String key = getKey(service, client);
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

    private String getKey(Service service, Client client)
    {
        return service.getName() + client.getId();
    }
}
