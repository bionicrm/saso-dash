package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBTimestampable;

import java.sql.Timestamp;
import java.util.Optional;

public interface DBAuthToken extends DBTimestampable, DBEntity
{
    String getAccess();

    Optional<String> getRefresh();

    Optional<Timestamp> getExpiresAt();
}
