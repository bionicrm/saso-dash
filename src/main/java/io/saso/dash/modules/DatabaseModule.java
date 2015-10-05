package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.saso.dash.auth.DashLiveToken;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.database.DB;
import io.saso.dash.database.DashDatabase;
import io.saso.dash.database.Database;
import io.saso.dash.server.*;

import java.sql.Connection;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Database.class).to(DashDatabase.class);

        bind(LiveToken.class).to(DashLiveToken.class);
    }

    @Provides @DB
    Connection provideDBConnection(Database database)
    {
        return database.getConnection();
    }
}
