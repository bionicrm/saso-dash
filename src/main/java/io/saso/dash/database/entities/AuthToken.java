package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntityWithTimestamps;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Represents a LiveToken from the "live_tokens" database.
 */
public interface AuthToken extends DBEntityWithTimestamps
{
    /**
     * Gets the access token. If {@link #getRefresh()} or
     * {@link #getExpiresAt()} is present, then this token may expire in the
     * future. As such, the refresh token returned by {@link #getRefresh()}
     * should be used to obtain a new access token.
     *
     * @return the access token
     */
    String getAccess();

    /**
     * Gets the refresh token. The column may be null. If
     * {@link #getExpiresAt()} is present, then this should be present and
     * vice-versa. This token, if present, should be used to obtain a new access
     * token if the token returned by {@link #getAccess()} is expired.
     *
     * @return an Optional of the refresh token
     */
    Optional<String> getRefresh();

    /**
     * Gets the expiration Timestamp. The column may be null. This AuthToken
     * should be considered invalid if authentication is attempted on or after
     * the returned Timestamp. If {@link #getRefresh()} is present, then this
     * should be present and vice-versa.
     *
     * @return an Optional of the expiration Timestamp
     */
    Optional<Timestamp> getExpiresAt();
}
