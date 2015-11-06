package io.saso.dash.redis.databases.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.RedisDatabase;
import me.mazeika.uconfig.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@Singleton
public class DashConcurrentConnections implements ConcurrentConnections
{
    private static final Logger logger = LogManager.getLogger();

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
        long start = System.nanoTime();

        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS))
        {
            connection.flushDB();
        }

        long end = System.nanoTime();
        logger.debug("Flushed Redis DB {} in about {}µs",
                RedisDatabase.CONCURRENT_CONNECTIONS.name(),
                TimeUnit.NANOSECONDS.toMicros(end - start));
    }

    @Override
    public synchronized boolean incrementIfAllowed(int userId)
    {
        long start = System.nanoTime();
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
        finally {
            long end = System.nanoTime();
            logger.debug("Incremented Redis concurrent connection for user " +
                            "{} in about {}µs", userId,
                    TimeUnit.NANOSECONDS.toMicros(end - start));
        }
    }

    @Override
    public synchronized void decrement(int userId)
    {
        long start = System.nanoTime();

        try (Jedis connection = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            connection.decr(String.valueOf(userId));
        }

        long end = System.nanoTime();
        logger.debug("Decremented Redis concurrent connection for user {} in " +
                        "about {}µs", userId,
                TimeUnit.NANOSECONDS.toMicros(end - start));
    }
}
