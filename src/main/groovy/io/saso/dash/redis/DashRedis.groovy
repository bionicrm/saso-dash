package io.saso.dash.redis

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.Protocol

@Singleton
class DashRedis implements Redis
{
    private final Config config

    private final Closure<JedisPool> connectionPool = {
        final JedisPoolConfig poolConfig = new JedisPoolConfig()

        final String host     = config.get('redis.host', '127.0.0.1')
        final int    port     = config.get('redis.port', 6379)
        final String password = config.get('redis.password', '')

        if (password.empty) {
            return new JedisPool(poolConfig, host, port)
        }
        else {
            return new JedisPool(poolConfig, host, port,
                    Protocol.DEFAULT_TIMEOUT, password)
        }
    }.memoize()

    @Inject
    DashRedis(Config config)
    {
        this.config = config
    }

    @Override
    Jedis getConnection(RedisDatabase db)
    {
        final Jedis connection = connectionPool().resource

        if (db.index != 0) {
            connection.select(db.index)
        }

        return connection
    }

    @Override
    <T> T use(RedisDatabase db, Closure<T> closure)
    {
        Jedis connection = null

        try {
            connection = getConnection(db)

            return closure.call(connection)
        }
        finally {
            connection?.close()
        }
    }
}
