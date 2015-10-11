package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntityWithTimestamps;

import java.util.Optional;

public interface User extends DBEntityWithTimestamps
{
    /**
     * Gets the name. May be a username or full, real name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the email. The column may be null.
     *
     * @return an Optional of the email
     */
    Optional<String> getEmail();
}
