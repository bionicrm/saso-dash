package io.saso.dash.services

import com.google.inject.name.Named
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.entities.DBLiveToken

public interface ServiceFactoryOLD
{
    fun createDBEntityProvider(liveToken: DBLiveToken): DBEntityProvider

    @Named("github")
    fun createGitHubService(): Service

    @Named("google")
    fun createGoogleService(): Service
}
