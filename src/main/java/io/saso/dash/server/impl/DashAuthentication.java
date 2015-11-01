package io.saso.dash.server.impl;

import com.google.inject.Inject;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.server.Authentication;

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
        return dbFetcher.fetch(DBLiveToken.class, "find_live_token", token);
    }
}
