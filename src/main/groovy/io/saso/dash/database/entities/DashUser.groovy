package io.saso.dash.database.entities

import java.sql.ResultSet
import java.sql.Timestamp

class DashUser implements User
{
    final String tableName = 'users'

    int              id
    String           name
    Optional<String> email
    Timestamp        createdAt
    Timestamp        updatedAt

    @Override
    void fill(ResultSet results)
    {
        id        = results.getInt 'id'
        name      = results.getString 'name'
        email     = Optional.ofNullable results.getString('email')
        createdAt = results.getTimestamp 'created_at'
        updatedAt = results.getTimestamp 'updated_at'
    }
}
