package io.saso.dash.database.entities

import java.sql.ResultSet
import java.sql.Timestamp

class DashAuthToken implements AuthToken
{
    final String tableName = 'auth_tokens'

    int                 id
    String              access
    Optional<String>    refresh
    Optional<Timestamp> expiresAt
    Timestamp           createdAt
    Timestamp           updatedAt

    @Override
    void fill(ResultSet results)
    {
        id        = results.getInt 'id'
        access    = results.getString 'access'
        refresh   = Optional.ofNullable results.getString('refresh')
        expiresAt = Optional.ofNullable results.getTimestamp('expires_at')
        createdAt = results.getTimestamp 'created_at'
        updatedAt = results.getTimestamp 'updated_at'
    }
}
