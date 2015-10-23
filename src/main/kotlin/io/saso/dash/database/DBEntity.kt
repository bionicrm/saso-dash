package io.saso.dash.database

import java.sql.ResultSet
import java.sql.SQLException

public interface DBEntity
{
    fun fill(resultSet: ResultSet)

    val id: Int

    val tableName: String
}
