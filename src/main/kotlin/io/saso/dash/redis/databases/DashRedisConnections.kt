package io.saso.dash.redis.databases

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.redis.Redis
import io.saso.dash.redis.RedisDatabase
import io.saso.dash.util.logger

@Singleton
public class DashRedisConnections
@Inject
constructor(val redis: Redis) : RedisConnections
{
    private object Limits
    {
        val maxConcurrentConnectionsPerUser = 3
    }

    override fun addIfAllowed(userId: Int): Boolean {
        val key = userId.toString()

        redis.getConnection(RedisDatabase.CONCURRENT_CONNECTIONS) use {
            it.setnx(key, "0")

            logger(this@DashRedisConnections) debug
                    "Concurrent connnections for $userId: ${it get key}"

            val connCount = it incr key

            if (connCount > Limits.maxConcurrentConnectionsPerUser) {
                it decr key
                return false
            }
        }

        return true
    }

    override fun remove(userId: Int) {
        redis getConnection RedisDatabase.CONCURRENT_CONNECTIONS use {
            it decr userId.toString()
        }
    }
}
