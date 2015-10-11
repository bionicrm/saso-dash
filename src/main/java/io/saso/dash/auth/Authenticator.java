package io.saso.dash.auth;

import io.saso.dash.database.entities.LiveToken;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Represents an authenticator for incoming WebSocket connections that provide
 * a {@code live_token}.
 */
public interface Authenticator
{
    /**
     * Finds and creates a LiveToken from the database. Returns an empty
     * Optional if the token is invalid (expired) or not found.
     *
     * @param token the token to find
     *
     * @return an Optional of a LiveToken
     *
     * @throws SQLException
     */
    Optional<LiveToken> findLiveToken(String token) throws Exception;
}
