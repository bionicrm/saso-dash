package io.saso.dash.database;

import java.sql.Timestamp;

public interface DBEntityWithTimestamps extends DBEntity
{
    /**
     * Gets the created at Timestamp.
     *
     * @return the created at Timestamp
     */
    Timestamp getCreatedAt();

    /**
     * Gets the updated at Timestamp.
     *
     * @return the updated at Timestamp
     */
    Timestamp getUpdatedAt();
}
