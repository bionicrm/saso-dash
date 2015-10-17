package io.saso.dash.database.entities

import java.sql.ResultSet
import java.sql.Timestamp

class DashLiveToken implements LiveToken
{
    final String tableName = 'live_tokens'

    int       id
    int       userId
    String    token
    String    ip
    Timestamp expiresAt
    Timestamp createdAt
    Timestamp updatedAt

    @Override
    void fillFromResultSet(ResultSet resultSet)
    {
        id        = resultSet.getInt 'id'
        userId    = resultSet.getInt 'user_id'
        token     = resultSet.getString 'token'
        ip        = resultSet.getString 'ip'
        expiresAt = resultSet.getTimestamp 'expires_at'
        createdAt = resultSet.getTimestamp 'created_at'
        updatedAt = resultSet.getTimestamp 'updated_at'
    }
}
