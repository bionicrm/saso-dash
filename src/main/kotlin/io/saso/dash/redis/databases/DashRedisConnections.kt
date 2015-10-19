package io.saso.dash.redis.databases

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import io.saso.dash.redis.Redis
import io.saso.dash.redis.RedisDatabase
import io.saso.dash.util.key
import io.saso.dash.util.logger

@Singleton
public class DashRedisConnections
@Inject
constructor(private val redis: Redis, private val config: Config) :
        RedisConnections
{
    private val maxConcurrentConnectionsPerUser =
            config.get("max-connections-per-user", 3)

    override fun addIfAllowed(userId: Int): Boolean {
        val key = key("user", userId)

        redis.getConnection(RedisDatabase.CONCURRENT_CONNECTIONS) use {
            val connCount = it incr key

            if (connCount > maxConcurrentConnectionsPerUser) {
                it decr key
                return false
            }

            logger(this@DashRedisConnections) debug
                    "Concurrent connnections for user $userId: $connCount"
        }

        return true
    }

    override fun remove(userId: Int) {
        redis getConnection RedisDatabase.CONCURRENT_CONNECTIONS use {
            it decr key("user", userId)
        }
    }
}
