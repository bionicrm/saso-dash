package io.saso.dash.database;

import java.sql.ResultSet;

public interface DBEntity
{
    boolean fillFromResultSet(ResultSet resultSet);
}
