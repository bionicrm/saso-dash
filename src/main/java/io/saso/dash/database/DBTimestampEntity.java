package io.saso.dash.database;

import java.sql.Timestamp;

public interface DBTimestampEntity extends DBEntity
{
    Timestamp getCreatedAt();

    Timestamp getUpdatedAt();
}
