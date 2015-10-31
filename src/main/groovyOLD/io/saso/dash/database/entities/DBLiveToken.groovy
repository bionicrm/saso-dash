package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps
import io.saso.dash.database.DBTimestampable

import java.sql.Timestamp

interface DBLiveToken extends DBTimestampable
{
    int getUserId()

    String getToken()

    String getIp()

    Timestamp getExpiresAt()
}
