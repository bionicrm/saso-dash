package io.saso.dash.database;

import java.sql.Timestamp;

public interface DBTimestampable
{
    Timestamp getCreatedAt();

    Timestamp getUpdatedAt();
}
