package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps
import io.saso.dash.database.DBTimestampable

import java.sql.Timestamp

interface DBAuthToken extends DBTimestampable
{
    String getAccess()

    Optional<String> getRefresh()

    Optional<Timestamp> getExpiresAt()
}
