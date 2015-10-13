package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.redis.DashRedis;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.tables.DashRedisConnections;
import io.saso.dash.redis.tables.RedisConnections;

public class RedisModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Redis.class).to(DashRedis.class);

        // tables
        bind(RedisConnections.class).to(DashRedisConnections.class);
    }
}
