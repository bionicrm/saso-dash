package io.saso.dash.redis.databases.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.Config;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.RedisDatabase;
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
                config.<Integer>get("limits.connections_per_user").orElse(3);
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
        String userIdStr = String.valueOf(userId);

        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            long currentConnections = connection.incr(userIdStr);

            if (currentConnections > connectionsPerUser) {
                connection.decr(userIdStr);
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
            connection.decr(String.valueOf(userId));
        }
    }
}
