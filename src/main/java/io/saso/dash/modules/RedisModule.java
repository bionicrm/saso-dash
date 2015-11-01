package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.config.Config;
import io.saso.dash.config.impl.DashConfig;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.impl.DashConcurrentConnections;
import io.saso.dash.redis.impl.DashRedis;

public class RedisModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Redis.class).to(DashRedis.class);

        // databases
        bind(ConcurrentConnections.class).to(DashConcurrentConnections.class);
    }
}
