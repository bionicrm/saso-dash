package io.saso.dash.auth;

import io.saso.dash.database.DBEntity;

import java.sql.Timestamp;
import java.util.Date;

public interface LiveToken extends DBEntity
{
    int getId();

    int getUserId();

    String getToken();

    String getIp();

    Timestamp getExpiresAt();

    Timestamp getCreatedAt();

    Timestamp getUpdatedAt();
}
