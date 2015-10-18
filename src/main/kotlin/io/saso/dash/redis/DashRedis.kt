package io.saso.dash.redis

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.Protocol

@Singleton
public class DashRedis
@Inject
constructor(private val config: Config): Redis
{
    private val pool: JedisPool by lazy {
        val poolConfig = JedisPoolConfig()

        val host     = config.get("redis.host", "127.0.0.1")
        val port     = config.get("redis.port", 6379)
        val password = config.get("redis.password", "")

        if (password.isEmpty()) {
            JedisPool(poolConfig, host, port)
        } else {
            JedisPool(
                    poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, password)
        }
    }

    init
    {
        // flush
        getConnection(RedisDatabase.CONCURRENT_CONNECTIONS).use({ conn ->
            conn.flushDB()
        })
    }

    override fun getConnection(db: RedisDatabase): Jedis
    {
        val connection = pool.resource

        connection select db.ordinal()

        return connection
    }
}
