package io.saso.dash.services

import com.google.inject.name.Named
import io.saso.dash.database.EntityProvider
import io.saso.dash.database.entities.LiveToken

public interface ServiceFactoryOLD
{
    fun createDBEntityProvider(liveToken: LiveToken): EntityProvider

    @Named("github")
    fun createGitHubService(): Service

    @Named("google")
    fun createGoogleService(): Service
}
