package io.saso.dash.database;

import java.sql.Connection;

public interface DBConnectionSupplier
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
