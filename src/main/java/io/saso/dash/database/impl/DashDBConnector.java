package io.saso.dash.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.database.DBConnector;
import me.mazeika.uconfig.Config;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DashDBConnector implements DBConnector
{
    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    private GenericObjectPool<PoolableConnection> connectionPool;

    @Inject
    public DashDBConnector(Config config)
    {
        this.config = config;
    }

    static
    {
        try {
            // load drivers
            Class.forName("org.apache.commons.dbcp2.PoolingDriver");
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Connection getConnection() throws Exception
    {
        return getConnectionPool().borrowObject();
    }

    /**
     * Gets the connection pool, creating it if necessary.
     *
     * @return the created/existing connection pool
     *
     * @throws SQLException
     */
    private synchronized
    GenericObjectPool<PoolableConnection> getConnectionPool()
            throws SQLException
    {

        if (connectionPool == null) {
            String url = String.format("jdbc:postgresql://%s:%d/%s",
                    config.getOrDefault("db.host", "127.0.0.1"),
                    config.getOrDefault("db.port", 5432),
                    config.getOrDefault("db.database", "postgres"));

            DriverManagerConnectionFactory connFactory =
                    new DriverManagerConnectionFactory(url,
                            config.getOrDefault("db.user", "postgres"),
                            config.getOrDefault("db.password", "postgres"));

            PoolableConnectionFactory poolableConnFactory =
                    new PoolableConnectionFactory(connFactory, null);

            connectionPool = new GenericObjectPool<>(poolableConnFactory);

            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(
                    "jdbc:apache:commons:dbcp:");

            poolableConnFactory.setPool(connectionPool);
            driver.registerPool("saso", connectionPool);
        }

        return connectionPool;
    }
}
