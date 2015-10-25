package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.saso.dash.database.DashDatabase
import io.saso.dash.database.DashDBEntityFetcher
import io.saso.dash.database.DashDBEntityProvider
import io.saso.dash.database.Database
import io.saso.dash.database.DBEntityFetcher
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBAuthToken
import io.saso.dash.database.entities.DashDBAuthToken
import io.saso.dash.database.entities.DashDBLiveToken
import io.saso.dash.database.entities.DashDBProvider
import io.saso.dash.database.entities.DashDBProviderUser
import io.saso.dash.database.entities.DashDBUser
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.database.entities.DBProvider
import io.saso.dash.database.entities.DBProviderUser
import io.saso.dash.database.entities.DBUser

class DatabaseModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Database to DashDatabase
        bind DBEntityFetcher to DashDBEntityFetcher

        install new FactoryModuleBuilder()
                .implement(DBEntityProvider, DashDBEntityProvider)
                .build(DBEntityProviderFactory)

        // entities
        bind DBAuthToken to DashDBAuthToken
        bind DBLiveToken to DashDBLiveToken
        bind DBProvider to DashDBProvider
        bind DBProviderUser to DashDBProviderUser
        bind DBUser to DashDBUser
    }
}
