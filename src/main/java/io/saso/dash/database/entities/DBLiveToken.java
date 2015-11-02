package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.sql.Timestamp;

public interface DBLiveToken extends DBTimestampEntity
{
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    int getUserId();

    /**
     * Gets the token.
     *
     * @return the token
     */
    String getToken();

    /**
     * Gets the IP.
     *
     * @return the IP
     */
    String getIp();

    /**
     * Gets when this live token expires.
     *
     * @return the expiry time
     */
    Timestamp getExpiresAt();
}
