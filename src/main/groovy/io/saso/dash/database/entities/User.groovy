package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps

interface User extends DBEntityWithTimestamps
{
    String getName()

    Optional<String> getEmail()
}
