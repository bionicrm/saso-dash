package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.database.DashDatabase
import io.saso.dash.database.DashEntityManager
import io.saso.dash.database.Database
import io.saso.dash.database.EntityManager
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
        bind EntityManager to DashEntityManager

        // entities
        bind AuthToken to DashAuthToken
        bind LiveToken to DashLiveToken
        bind Provider to DashProvider
        bind ProviderUser to DashProviderUser
        bind User to DashUser
    }
}
