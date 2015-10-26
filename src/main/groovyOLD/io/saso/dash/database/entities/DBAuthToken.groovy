package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps

import java.sql.Timestamp

interface DBAuthToken extends DBEntityWithTimestamps
{
    String getAccess()

    Optional<String> getRefresh()

    Optional<Timestamp> getExpiresAt()
}
