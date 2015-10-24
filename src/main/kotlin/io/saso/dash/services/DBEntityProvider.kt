package io.saso.dash.services

import io.saso.dash.database.entities.*

public interface DBEntityProviderOLD
{
    fun liveToken(): DBLiveToken

    fun user(): DBUser

    fun provider(service: String): DBProvider

    fun providerUser(service: String): DBProviderUser

    fun authToken(service: String): DBAuthToken
}
