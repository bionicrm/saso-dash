package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBTimestampable;

public interface DBServiceUser extends DBTimestampable, DBEntity
{
    int getUserId();

    int getServiceId();

    int getAuthTokenId();

    String getServiceUniqueId();
}
