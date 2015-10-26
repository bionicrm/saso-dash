package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.redis.DashRedis
import io.saso.dash.redis.Redis
import io.saso.dash.redis.databases.ConcurrentConnections
import io.saso.dash.redis.databases.DashConcurrentConnections

class RedisModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Redis to DashRedis

        // databases
        bind ConcurrentConnections to DashConcurrentConnections
        // TODO bind RedisServices to DashRedisServices
    }
}
