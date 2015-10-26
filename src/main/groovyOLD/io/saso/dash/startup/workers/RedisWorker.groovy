package io.saso.dash.startup.workers

import com.google.inject.Inject
import io.saso.dash.redis.Redis
import io.saso.dash.redis.RedisDatabase
import io.saso.dash.startup.StartupWorker
import redis.clients.jedis.Jedis

class RedisWorker implements StartupWorker
{
    private final Redis redis

    @Inject
    RedisWorker(Redis redis)
    {
        this.redis = redis
    }

    @Override
    void work()
    {
        redis.use(RedisDatabase.CONCURRENT_CONNECTIONS, { Jedis connection ->
            connection.flushDB()
        })
    }
}
