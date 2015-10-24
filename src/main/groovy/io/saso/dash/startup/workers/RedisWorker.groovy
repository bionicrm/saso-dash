package io.saso.dash.startup.workers

import com.google.inject.Inject
import io.saso.dash.redis.Redis
import io.saso.dash.Worker

class RedisWorker implements Worker
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
