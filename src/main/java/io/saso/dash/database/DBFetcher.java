package io.saso.dash.database;

import java.util.Optional;

public interface DBFetcher
{
    /**
     * Fetches an entity from the database.
     *
     * @param entityClass the class of the entity to retrieve; can be an
     *                    interface with a Guice bound implementation
     * @param scriptName the name of the SQL script to execute
     * @param params the SQL parameters
     *
     * @return an optional of the entity; empty if not found or an error occurs
     *
     * @see DBScriptRepository#getSQL(String)
     */
    <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String scriptName, Object... params);
}
