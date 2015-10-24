package io.saso.dash.database.entities

import java.sql.ResultSet
import java.sql.Timestamp

class DashDBLiveToken implements DBLiveToken
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
    void fill(ResultSet results)
    {
        id        = results.getInt 'id'
        userId    = results.getInt 'user_id'
        token     = results.getString 'token'
        ip        = results.getString 'ip'
        expiresAt = results.getTimestamp 'expires_at'
        createdAt = results.getTimestamp 'created_at'
        updatedAt = results.getTimestamp 'updated_at'
    }
}
