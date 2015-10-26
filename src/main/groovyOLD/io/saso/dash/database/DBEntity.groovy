package io.saso.dash.database

import java.sql.ResultSet

interface DBEntity
{
    void fill(ResultSet results)

    int getId()

    String getTableName()
}
