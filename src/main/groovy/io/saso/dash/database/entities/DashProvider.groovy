package io.saso.dash.database.entities

import java.sql.ResultSet

class DashProvider implements Provider
{
    final String tableName = 'providers'

    int    id
    String name

    @Override
    void fillFromResultSet(ResultSet resultSet)
    {
        id   = resultSet.getInt 'id'
        name = resultSet.getString 'name'
    }
}
