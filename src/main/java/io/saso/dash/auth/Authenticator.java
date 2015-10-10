package io.saso.dash.auth;

import java.sql.SQLException;
import java.util.Optional;

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
    Optional<LiveToken> findLiveToken(String token) throws SQLException;
}
