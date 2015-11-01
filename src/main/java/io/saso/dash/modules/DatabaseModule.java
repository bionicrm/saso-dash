package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import io.saso.dash.database.DBConnector;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.entities.*;
import io.saso.dash.database.entities.impl.*;
import io.saso.dash.database.impl.DashDBConnector;
import io.saso.dash.database.impl.DashDBFetcher;
import io.saso.dash.database.scripts.EntityReturnableSQLScript;
import io.saso.dash.database.scripts.SQLScriptFactory;
import io.saso.dash.database.scripts.impl.DashFindAuthTokenSQLScript;
import io.saso.dash.database.scripts.impl.DashFindLiveTokenSQLScript;
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
        bind(DBFetcher.class).to(DashDBFetcher.class);

        // entities
        bind(DBAuthToken.class).to(DashDBAuthToken.class);
        bind(DBLiveToken.class).to(DashDBLiveToken.class);
        bind(DBService.class).to(DashDBService.class);
        bind(DBServiceUser.class).to(DashDBServiceUser.class);
        bind(DBUser.class).to(DashDBUser.class);

        // scripts
        install(new FactoryModuleBuilder()
                .implement(EntityReturnableSQLScript.class,
                        Names.named("find_auth_token"),
                        DashFindAuthTokenSQLScript.class)
                .implement(EntityReturnableSQLScript.class,
                        Names.named("find_live_token"),
                        DashFindLiveTokenSQLScript.class)
                .build(SQLScriptFactory.class));
    }
}
