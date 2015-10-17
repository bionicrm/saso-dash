package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps

import java.sql.Timestamp

interface LiveToken extends DBEntityWithTimestamps
{
    int getUserId()

    String getToken()

    String getIp()

    Timestamp getExpiresAt()
}
