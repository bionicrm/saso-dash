package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.throwingproviders.CheckedProvides;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import io.saso.dash.auth.DashLiveToken;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.database.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Database.class).to(DashDatabase.class);
        bind(DatabaseExecutor.class).to(DashDatabaseExecutor.class);
        bind(LiveToken.class).to(DashLiveToken.class);
    }
}
