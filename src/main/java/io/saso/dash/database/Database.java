package io.saso.dash.database;

import java.sql.Connection;

public interface Database
{
    /**
     * Gets a database connection from the connection pool.
     *
     * @return a database connection
     *
     * @throws Exception
     */
    Connection getConnection() throws Exception;
}
