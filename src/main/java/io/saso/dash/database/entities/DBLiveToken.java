package io.saso.dash.database.entities;

import io.saso.dash.database.DBTimestampEntity;

import java.sql.Timestamp;

public interface DBLiveToken extends DBTimestampEntity
{
    int getUserId();

    String getToken();

    String getIp();

    Timestamp getExpiresAt();
}
