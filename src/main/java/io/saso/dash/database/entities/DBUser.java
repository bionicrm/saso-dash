package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.util.Optional;

public interface DBUser extends DBTimestampEntity
{
    String getName();

    Optional<String> getEmail();
}
