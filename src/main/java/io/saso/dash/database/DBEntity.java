package io.saso.dash.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBEntity
{
    void fillFromResultSet(ResultSet resultSet) throws SQLException;
}
