package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntityWithTimestamps;

public interface ProviderUser extends DBEntityWithTimestamps
{
    /**
     * Gets the user's ID. The column has a foreign key restraint for a user.
     *
     * @return the user's ID
     *
     * @see User
     */
    int getUserId();

    /**
     * Gets the providers's ID. The column has a foreign key restraint for a
     * provider.
     *
     * @return the provider's ID
     *
     * @see Provider
     */
    int getProviderId();

    /**
     * Gets the auth tokens's ID. The column has a foreign key restraint for an
     * auth token.
     *
     * @return the auth tokens's ID
     *
     * @see AuthToken
     */
    int getAuthTokenId();

    /**
     * Gets the provider's unique ID. Despite the column name, the column does
     * <i>not</i> have a unique flag.
     *
     * @return the provider's unique ID.
     */
    String getProviderUniqueId();
}
