package io.saso.dash.database;

import org.apache.commons.dbcp2.PoolableConnection;

import java.sql.SQLException;

/**
 * Represents a database. Implements a connection pool for thread-safe
 * connection reusing.
 */
public interface DatabaseOLD
{
    /**
     * Gets a poolable database Connection. When it is finished being used,
     * return it back to the pool with {@link PoolableConnection#close()} or use
     * try-with-resources.
     *
     * @return a database PoolableConnection
     *
     * @throws SQLException
     */
    PoolableConnection getConnection() throws SQLException;

    /**
     * Closes the global database Connection pool.
     *
     * @throws SQLException
     */
    void closePool() throws SQLException;
}
