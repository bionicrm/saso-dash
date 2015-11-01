package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.database.DBConnector;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.DBScriptRepository;
import io.saso.dash.database.entities.*;
import io.saso.dash.database.entities.impl.*;
import io.saso.dash.database.impl.DashDBConnector;
import io.saso.dash.database.impl.DashDBFetcher;
import io.saso.dash.database.impl.DashDBScriptRepository;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(DBConnector.class).to(DashDBConnector.class);
        bind(DBFetcher.class).to(DashDBFetcher.class);
        bind(DBScriptRepository.class).to(DashDBScriptRepository.class);

        // entities
        bind(DBAuthToken.class).to(DashDBAuthToken.class);
        bind(DBLiveToken.class).to(DashDBLiveToken.class);
        bind(DBService.class).to(DashDBService.class);
        bind(DBServiceUser.class).to(DashDBServiceUser.class);
        bind(DBUser.class).to(DashDBUser.class);
    }
}
