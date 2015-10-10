package io.saso.dash.database;

import org.apache.commons.dbcp2.PoolableConnection;

import java.sql.SQLException;

public interface Database
{
    /**
     * Gets a poolable database Connection. When it is finished being used,
     * return it back to the pool with {@link PoolableConnection#close()}.
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
