package io.saso.dash.services

import io.saso.dash.database.entities.AuthToken
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.database.entities.Provider
import io.saso.dash.database.entities.ProviderUser
import io.saso.dash.database.entities.User

interface DBEntityProvider
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
     * Gets the Provider entity for the service this is for.
     *
     * @return the provider
     */
    Provider getProvider()

    /**
     * Gets the ProviderUser for the service this is for.
     *
     * @return the provider user
     */
    ProviderUser getProviderUser()

    /**
     * Gets the AuthToken for the service this is for.
     *
     * @return the auth token
     */
    AuthToken getAuthToken()
}
