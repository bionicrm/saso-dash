package io.saso.dash.server;

import io.saso.dash.database.entities.DBLiveToken;

import java.util.Optional;

public interface Authentication
{
    /**
     * Gets a live token from the DB based off the given token. If the token is
     * invalid or not found, an empty optional is returned.
     *
     * @param token the token to authenticate
     *
     * @return an optional of the live token; empty if not found or invalid
     */
    Optional<DBLiveToken> authenticate(String token);
}
