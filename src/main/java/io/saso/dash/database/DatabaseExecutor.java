package io.saso.dash.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseExecutor
{
    ResultSet execute(String sql, Object... args) throws SQLException;

    void close() throws SQLException;
}
