package io.saso.dash.database.entities

import java.sql.ResultSet

class DashDBProvider implements DBService
{
    final String tableName = 'providers'

    int    id
    String name

    @Override
    void fill(ResultSet results)
    {
        id   = results.getInt 'id'
        name = results.getString 'name'
    }
}
