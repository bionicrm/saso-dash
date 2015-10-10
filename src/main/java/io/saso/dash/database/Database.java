package io.saso.dash.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database
{
    /**
     * Gets a database Connection. The connection is local to the calling
     * thread, meaning each thread has its own connection. Therefore, each query
     * will block only in the calling thread, not on all threads.
     *
     * @return a database Connection
     *
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the database Connection that is local to the calling thread. It is
     * also removed from the thread, allowing subsequent calls to
     * {@link #getConnection()} to return a new connection.
     *
     * @see #getConnection()
     *
     * @throws SQLException
     */
    void closeConnection() throws SQLException;

    /**
     * Closes the global database Connection pool.
     *
     * @throws SQLException
     */
    void closePool() throws SQLException;
}
