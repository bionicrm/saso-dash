package io.saso.dash.database;

import io.saso.dash.database.scripts.EntityReturnableSQLScript;

import java.util.Optional;

public interface DBFetcher
{
    /**
     * Fetches an entity from the database.
     *
     * @param script the script to use to fetch the entity
     *
     * @return an optional of the entity; empty if not found
     */
    <T extends DBEntity> Optional<T> fetch(EntityReturnableSQLScript script);
}
