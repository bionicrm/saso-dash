package io.saso.dash.services

import com.google.inject.name.Named
import io.saso.dash.database.entities.LiveToken

public interface ServiceFactory
{
    fun createDBEntityProvider(liveToken: LiveToken): DBEntityProvider

    @Named("github")
    fun createGitHubService(): Service

    @Named("google")
    fun createGoogleService(): Service
}
