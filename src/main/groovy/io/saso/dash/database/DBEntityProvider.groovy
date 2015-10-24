package io.saso.dash.database

import io.saso.dash.database.entities.DBAuthToken
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.database.entities.DBProvider
import io.saso.dash.database.entities.DBProviderUser
import io.saso.dash.database.entities.DBUser
import io.saso.dash.services.Service

interface DBEntityProvider
{
    /**
     * Gets the LiveToken entity for the user.
     *
     * @return the live token
     */
    DBLiveToken getLiveToken()

    /**
     * Gets the User entity for the user.
     *
     * @return the user
     */
    DBUser getUser()

    /**
     * Gets the Provider entity for the specified service.
     *
     * @param service the provider's associated service
     *
     * @return the provider
     */
    DBProvider getProvider(Service service)

    /**
     * Gets the ProviderUser for the specified service.
     *
     * @param service the provider user's associated service
     *
     * @return the provider user
     */
    DBProviderUser getProviderUser(Service service)

    /**
     * Gets the AuthToken for the specified service.
     *
     * @param service the auth token's associated service
     *
     * @return the auth token
     */
    DBAuthToken getAuthToken(Service service)
}
