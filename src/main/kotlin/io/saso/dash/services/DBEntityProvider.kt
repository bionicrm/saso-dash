package io.saso.dash.services

import io.saso.dash.database.entities.*

public interface DBEntityProvider
{
    fun liveToken(): LiveToken

    fun user(): User

    fun provider(service: String): Provider

    fun providerUser(service: String): ProviderUser

    fun authToken(service: String): AuthToken
}
