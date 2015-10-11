package io.saso.dash.redis.tables;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.RedisDatabase;
import org.apache.logging.log4j.LogManager;
import redis.clients.jedis.Jedis;

@Singleton
public class DashRedisConnections implements RedisConnections
{
    private static final int MAX_CONCURRENT_CONNECTIONS_PER_USER = 3;

    private final Redis redis;

    @Inject
    public DashRedisConnections(Redis redis)
    {
        this.redis = redis;

        clear();
    }

    @Override
    public boolean addIfAllowed(int userId)
    {
        final String key = String.valueOf(userId);

        try (Jedis conn = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            final long connCount = conn.incr(key);

            LogManager.getLogger().debug("{} concurrent connections for user " +
                    "{}", connCount, userId);

            if (connCount > MAX_CONCURRENT_CONNECTIONS_PER_USER) {
                conn.decr(key);
                return false;
            }
        }

        return true;
    }

    @Override
    public void remove(int userId)
    {
        try (Jedis conn = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            conn.decr(String.valueOf(userId));
        }
    }

    private void clear()
    {
        try (Jedis conn = redis.getConnection(
                RedisDatabase.CONCURRENT_CONNECTIONS)) {
            conn.flushDB();
        }
    }
}
