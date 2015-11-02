package io.saso.dash.server.impl;

import com.google.inject.Inject;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.server.Authentication;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class DashAuthentication implements Authentication
{
    private final DBFetcher dbFetcher;

    @Inject
    public DashAuthentication(DBFetcher dbFetcher)
    {
        this.dbFetcher = dbFetcher;
    }

    @Override
    public Optional<DBLiveToken> authenticate(String token)
    {
        Optional<DBLiveToken> liveTokenOptional =
                dbFetcher.fetch(DBLiveToken.class, "find_live_token", token);

        if (liveTokenOptional.isPresent() && isValid(liveTokenOptional.get())) {
            return liveTokenOptional;
        }

        return Optional.empty();
    }

    /**
     * Validates the given live token.
     *
     * @param liveToken the live token to validate
     *
     * @return {@code true} if and only if the live token is valid
     */
    private boolean isValid(DBLiveToken liveToken)
    {
        return liveToken.getExpiresAt().after(Timestamp.from(Instant.now()));
    }
}
