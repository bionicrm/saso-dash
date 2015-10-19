package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.redis.DashRedis
import io.saso.dash.redis.Redis
import io.saso.dash.redis.databases.DashRedisConnections
import io.saso.dash.redis.databases.DashRedisServices
import io.saso.dash.redis.databases.RedisConnections
import io.saso.dash.redis.databases.RedisServices

class RedisModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Redis to DashRedis

        // databases
        bind RedisConnections to DashRedisConnections
        bind RedisServices to DashRedisServices
    }
}
