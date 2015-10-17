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
    void fillFromResultSet(ResultSet resultSet)
    {
        id        = resultSet.getInt 'id'
        name      = resultSet.getString 'name'
        email     = Optional.ofNullable resultSet.getString('email')
        createdAt = resultSet.getTimestamp 'created_at'
        updatedAt = resultSet.getTimestamp 'updated_at'
    }
}
