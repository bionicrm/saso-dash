package io.saso.dash.database

import io.saso.dash.database.entities.AuthToken
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.database.entities.Provider
import io.saso.dash.database.entities.ProviderUser
import io.saso.dash.database.entities.User
import io.saso.dash.services.Service

interface EntityProvider
{
    /**
     * Gets the LiveToken entity for the user.
     *
     * @return the live token
     */
    LiveToken getLiveToken()

    /**
     * Gets the User entity for the user.
     *
     * @return the user
     */
    User getUser()

    /**
     * Gets the Provider entity for the specified service.
     *
     * @param service the provider's associated service
     *
     * @return the provider
     */
    Provider getProvider(Service service)

    /**
     * Gets the ProviderUser for the specified service.
     *
     * @param service the provider user's associated service
     *
     * @return the provider user
     */
    ProviderUser getProviderUser(Service service)

    /**
     * Gets the AuthToken for the specified service.
     *
     * @param service the auth token's associated service
     *
     * @return the auth token
     */
    AuthToken getAuthToken(Service service)
}
