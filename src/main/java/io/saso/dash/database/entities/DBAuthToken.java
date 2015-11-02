package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.sql.Timestamp;
import java.util.Optional;

public interface DBAuthToken extends DBTimestampEntity
{
    /**
     * Gets the access token.
     *
     * @return the access token
     */
    String getAccess();

    /**
     * Gets the refresh token. Can be {@code null} in the DB.
     *
     * @return an optional of the refresh token
     */
    Optional<String> getRefresh();

    /**
     * Gets when this auth token expires. Can be {@code null} in the DB.
     *
     * @return an optional of the expiry time
     */
    Optional<Timestamp> getExpiresAt();
}
