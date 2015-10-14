package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;

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
