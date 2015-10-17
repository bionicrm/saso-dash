package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.database.*;
import io.saso.dash.database.entities.*;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Database.class).to(DashDatabase.class);
        bind(EntityManager.class).to(DashEntityManager.class);

        // entities
        bind(LiveToken.class).to(DashLiveToken.class);
        bind(AuthToken.class).to(DashAuthToken.class);
        bind(User.class).to(DashUser.class);
        bind(ProviderUser.class).to(DashProviderUser.class);
        bind(Provider.class).to(DashProvider.class);
    }
}
