package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps
import io.saso.dash.database.DBTimestampable

interface DBUser extends DBTimestampable
{
    String getName()

    Optional<String> getEmail()
}
