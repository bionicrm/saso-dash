package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBEntityWithTimestamps;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Represents a LiveToken from the "live_tokens" database.
 */
public interface Provider extends DBEntity
{
    /**
     * Gets the name of the provider.
     *
     * @return the name
     */
    String getName();
}
