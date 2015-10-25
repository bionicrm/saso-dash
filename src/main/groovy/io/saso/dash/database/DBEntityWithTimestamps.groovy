package io.saso.dash.database

import java.sql.Timestamp

interface DBEntityWithTimestamps extends DBEntity
{
    Timestamp getCreatedAt()

    Timestamp getUpdatedAt()
}
