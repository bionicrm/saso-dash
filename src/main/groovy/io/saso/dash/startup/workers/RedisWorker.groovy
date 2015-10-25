package io.saso.dash.startup.workers

import com.google.inject.Inject
import io.saso.dash.redis.Redis
import io.saso.dash.startup.StartupWorker

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
        redis.initialize()
    }
}
