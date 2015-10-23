package io.saso.dash.services

import com.google.inject.Inject

class DashServiceCreator implements ServiceCreator
{
    private final ServiceFactory serviceFactory

    @Inject
    DashServiceCreator(ServiceFactory serviceFactory)
    {
        this.serviceFactory = serviceFactory
    }

    @Override
    Set<Service> createServices()
    {
        // TODO: inject preferences, read, and create appropriate services

        // temporary:
        [serviceFactory.createGitHubService(),
         serviceFactory.createGoogleService()]
    }
}
