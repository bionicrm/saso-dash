package io.saso.dash.redis.databases

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import io.saso.dash.redis.Redis
import io.saso.dash.redis.RedisDatabase
import redis.clients.jedis.Jedis

@Singleton
class DashConcurrentConnections implements ConcurrentConnections
{
    private final Redis redis
    private final int maxConnectionsPerUser

    @Inject
    DashConcurrentConnections(Redis redis, Config config)
    {
        this.redis = redis

        maxConnectionsPerUser = config.get('max-connections-per-user', 3)
    }

    @Override
    boolean addConnection(int userId)
    {
        redis.use(RedisDatabase.CONCURRENT_CONNECTIONS, { Jedis connection ->
            final int connectionCount = connection.incr("user:$userId")

            if (connectionCount > maxConnectionsPerUser) {
                connection.decr("user:$userId")

                return false
            }

            return true
        })
    }

    @Override
    void removeConnection(int userId)
    {
        redis.use(RedisDatabase.CONCURRENT_CONNECTIONS, { Jedis connection ->
            connection.decr("user:$userId")
        })
    }
}
