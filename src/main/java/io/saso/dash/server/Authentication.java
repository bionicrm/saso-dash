package io.saso.dash.server;

import io.saso.dash.database.entities.DBLiveToken;

import java.util.Optional;

public interface Authentication
{
    Optional<DBLiveToken> authenticate(String token);
}
