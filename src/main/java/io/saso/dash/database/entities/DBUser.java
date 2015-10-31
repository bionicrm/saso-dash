package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBTimestampable;

import java.util.Optional;

public interface DBUser extends DBTimestampable, DBEntity
{
    String getName();

    Optional<String> getEmail();
}
