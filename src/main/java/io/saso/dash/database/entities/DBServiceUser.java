package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

public interface DBServiceUser extends DBTimestampEntity
{
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    int getUserId();

    /**
     * Gets the service ID.
     *
     * @return the service ID
     */
    int getServiceId();

    /**
     * Gets the auth token ID.
     *
     * @return the auth token ID
     */
    int getAuthTokenId();

    /**
     * Gets the unique service ID.
     *
     * @return the unique service ID.
     */
    String getServiceUniqueId();
}
