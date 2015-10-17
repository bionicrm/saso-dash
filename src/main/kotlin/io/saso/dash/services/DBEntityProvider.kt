package io.saso.dash.services

import io.saso.dash.database.entities.AuthToken
import io.saso.dash.database.entities.Provider
import io.saso.dash.database.entities.ProviderUser
import io.saso.dash.database.entities.User

public interface DBEntityProvider
{
    fun user(): User

    fun provider(service: Service): Provider

    fun providerUser(service: Service): ProviderUser

    fun authToken(service: Service): AuthToken
}
