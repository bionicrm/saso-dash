package io.saso.dash.auth;

import io.saso.dash.database.Entity;

import java.util.Date;

public interface LiveToken extends Entity
{
    int getId();

    int getUserId();

    String getToken();

    String getIp();

    Date getExpiresAt();

    Date getCreatedAt();

    Date getUpdatedAt();
}
