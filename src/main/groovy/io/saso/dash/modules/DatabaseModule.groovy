package io.saso.dash.modules

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.saso.dash.database.DashDatabase
import io.saso.dash.database.DashEntityFetcher
import io.saso.dash.database.DashEntityProvider
import io.saso.dash.database.Database
import io.saso.dash.database.EntityFetcher
import io.saso.dash.database.EntityProvider
import io.saso.dash.database.EntityProviderFactory
import io.saso.dash.database.entities.AuthToken
import io.saso.dash.database.entities.DashAuthToken
import io.saso.dash.database.entities.DashLiveToken
import io.saso.dash.database.entities.DashProvider
import io.saso.dash.database.entities.DashProviderUser
import io.saso.dash.database.entities.DashUser
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.database.entities.Provider
import io.saso.dash.database.entities.ProviderUser
import io.saso.dash.database.entities.User

class DatabaseModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Database to DashDatabase
        bind EntityFetcher to DashEntityFetcher
        bind EntityProvider to DashEntityProvider

        install new FactoryModuleBuilder().build(EntityProviderFactory)

        // entities
        bind AuthToken to DashAuthToken
        bind LiveToken to DashLiveToken
        bind Provider to DashProvider
        bind ProviderUser to DashProviderUser
        bind User to DashUser
    }
}
