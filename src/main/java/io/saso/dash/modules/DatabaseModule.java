package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.database.DBConnector;
import io.saso.dash.database.entities.*;
import io.saso.dash.database.entities.impl.*;
import io.saso.dash.database.impl.DashDBConnector;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.ConcurrentConnections;
import io.saso.dash.redis.databases.impl.DashConcurrentConnections;
import io.saso.dash.redis.impl.DashRedis;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(DBConnector.class).to(DashDBConnector.class);

        // entities
        bind(DBAuthToken.class).to(DashDBAuthToken.class);
        bind(DBLiveToken.class).to(DashDBLiveToken.class);
        bind(DBService.class).to(DashDBService.class);
        bind(DBServiceUser.class).to(DashDBServiceUser.class);
        bind(DBUser.class).to(DashDBUser.class);
    }
}
