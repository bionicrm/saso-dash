package io.saso.dash.redis.databases.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.RedisDatabase;
import me.mazeika.uconfig.Config;
import redis.clients.jedis.Jedis;

@Singleton
public class DashConcurrentConnections implements ConcurrentConnections
{
    private final Redis redis;
    private final int connectionsPerUser;

    @Inject
    public DashConcurrentConnections(Redis redis, Config config)
    {
        this.redis = redis;
        connectionsPerUser =
                config.getOrDefault("limits.connections_per_user", 3);
    }

    @Override
    public void initialize()
    {
        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS))
        {
            connection.flushDB();
        }
    }

    @Override
    public synchronized boolean incrementIfAllowed(int userId)
    {
        String key = getKey(userId);

        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            long currentConnections = connection.incr(key);

            if (currentConnections > connectionsPerUser) {
                connection.decr(key);
                return false;
            }

            return true;
        }
    }

    @Override
    public synchronized void decrement(int userId)
    {
        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            connection.decr(getKey(userId));
        }
    }

    private String getKey(int userId)
    {
        return String.valueOf(userId);
    }
}
