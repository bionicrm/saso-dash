package io.saso.dash.database;

import java.sql.Timestamp;

public interface DBTimestampEntity extends DBEntity
{
    /**
     * Gets when this entity was created in the DB.
     *
     * @return when this entity was created in the DB
     */
    Timestamp getCreatedAt();

    /**
     * Gets when this entity was last updated.
     *
     * @return when this entity was last updated
     */
    Timestamp getUpdatedAt();
}
