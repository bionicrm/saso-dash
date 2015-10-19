package io.saso.dash.redis.databases

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.redis.Redis
import io.saso.dash.redis.RedisDatabase
import io.saso.dash.util.key

@Singleton
public class DashRedisServices
@Inject
constructor(private val redis: Redis) : RedisServices
{
    override fun get(userId: Int, service: String): String
    {
        redis.getConnection(RedisDatabase.SERVICES) use {
            synchronized(Lock.getSet, {
                return it get key("user", userId, service) ?: ""
            })
        }
    }

    override fun set(userId: Int, service: String, data: (String) -> String)
    {
        val key = key("user", userId, service)

        redis.getConnection(RedisDatabase.SERVICES) use {
            synchronized(Lock.getSet, {
                val currentData = it get key

                it.set(key, data(currentData))
            })
        }
    }

    override fun remove(userId: Int, service: String)
    {
        redis getConnection RedisDatabase.SERVICES use {
            it del key("user", userId, service)
        }
    }

    private object Lock
    {
        val getSet = Any()
    }
}
