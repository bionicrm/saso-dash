package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.saso.dash.redis.DashRedis;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.tables.DashRedisConnections;
import io.saso.dash.redis.tables.RedisConnections;
import io.saso.dash.services.DashServiceManager;
import io.saso.dash.services.Service;
import io.saso.dash.services.ServiceManager;
import io.saso.dash.services.github.GitHubService;
import io.saso.dash.services.google.GoogleService;

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
