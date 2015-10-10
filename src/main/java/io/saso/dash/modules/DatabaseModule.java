package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.auth.DashLiveToken;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.database.DashDatabase;
import io.saso.dash.database.Database;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Database.class).to(DashDatabase.class);
        bind(LiveToken.class).to(DashLiveToken.class);
    }
}
