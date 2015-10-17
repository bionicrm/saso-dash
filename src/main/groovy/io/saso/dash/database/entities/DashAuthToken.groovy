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
    void fillFromResultSet(ResultSet resultSet)
    {
        id        = resultSet.getInt 'id'
        access    = resultSet.getString 'access'
        refresh   = Optional.ofNullable resultSet.getString('refresh')
        expiresAt = Optional.ofNullable resultSet.getTimestamp('expires_at')
        createdAt = resultSet.getTimestamp('created_at')
        updatedAt = resultSet.getTimestamp('updated_at')
    }
}
