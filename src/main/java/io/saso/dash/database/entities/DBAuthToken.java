package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.sql.Timestamp;
import java.util.Optional;

public interface DBAuthToken extends DBTimestampEntity
{
    String getAccess();

    Optional<String> getRefresh();

    Optional<Timestamp> getExpiresAt();
}
