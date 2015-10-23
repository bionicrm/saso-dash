package io.saso.dash.services

import com.google.inject.name.Named

interface ServiceFactory
{
    @Named('github')
    Service createGitHubService()

    @Named('google')
    Service createGoogleService()
}
