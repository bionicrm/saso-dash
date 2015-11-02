package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.util.Optional;

public interface DBUser extends DBTimestampEntity
{
    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the email. Can be {@code null} in the DB.
     *
     * @return an optional of the email
     */
    Optional<String> getEmail();
}
