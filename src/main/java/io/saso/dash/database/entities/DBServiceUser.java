package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

public interface DBServiceUser extends DBTimestampEntity
{
    int getUserId();

    int getServiceId();

    int getAuthTokenId();

    String getServiceUniqueId();
}
