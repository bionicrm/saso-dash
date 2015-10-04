package io.saso.dash.database;

import java.sql.ResultSet;

public interface Entity
{
    boolean fillFromDatabase(ResultSet resultSet);
}
