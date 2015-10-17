package io.saso.dash.database

import java.sql.Timestamp

public interface DBEntityWithTimestamps : DBEntity
{
    val createdAt: Timestamp

    val updatedAt: Timestamp
}
