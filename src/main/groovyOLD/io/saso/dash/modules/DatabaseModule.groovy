package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.saso.dash.database.impl.DashDBConnector
import io.saso.dash.database.DashDBEntityFetcher
import io.saso.dash.database.DashDBEntityProvider
import io.saso.dash.database.DBConnector
import io.saso.dash.database.DBFetcher
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityProviderFactory
import io.saso.dash.database.entities.DBAuthToken
import io.saso.dash.database.entities.DashDBAuthToken
import io.saso.dash.database.entities.DashDBLiveToken
import io.saso.dash.database.entities.DashDBProvider
import io.saso.dash.database.entities.DashDBProviderUser
import io.saso.dash.database.entities.DashDBUser
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.database.entities.DBService
import io.saso.dash.database.entities.DBServiceUser
import io.saso.dash.database.entities.DBUser

class DatabaseModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind DBConnector to DashDBConnector
        bind DBFetcher to DashDBEntityFetcher

        install new FactoryModuleBuilder()
                .implement(DBEntityProvider, DashDBEntityProvider)
                .build(DBEntityProviderFactory)

        // entities
        bind DBAuthToken to DashDBAuthToken
        bind DBLiveToken to DashDBLiveToken
        bind DBService to DashDBProvider
        bind DBServiceUser to DashDBProviderUser
        bind DBUser to DashDBUser
    }
}
