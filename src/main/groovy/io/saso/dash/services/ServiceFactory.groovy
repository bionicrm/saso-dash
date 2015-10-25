package io.saso.dash.services

import com.google.inject.name.Named

interface ServiceFactory
{
    @Named('github')
    ServiceName createGitHubService()

    @Named('google')
    ServiceName createGoogleService()
}
