package io.saso.dash.database.entities;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBTimestampable;

import java.sql.Timestamp;

public interface DBLiveToken extends DBTimestampable, DBEntity
{
    int getUserId();

    String getToken();

    String getIp();

    Timestamp getExpiresAt();
}
