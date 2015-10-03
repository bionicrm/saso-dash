package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.saso.dash.database.DashDatabase;
import io.saso.dash.database.Database;
import io.saso.dash.server.*;

public class DatabaseModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Database.class).to(DashDatabase.class);
    }
}
