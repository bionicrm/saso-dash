package io.saso.dash.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.Config;
import io.saso.dash.database.DBConnector;
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
                    config.<String>get("db.host").orElse("127.0.0.1"),
                    config.<Integer>get("db.port").orElse(5432),
                    config.<String>get("db.database").orElse("postgres"));

            DriverManagerConnectionFactory connFactory =
                    new DriverManagerConnectionFactory(url,
                            config.<String>get("db.user").orElse("postgres"),
                            config.<String>get("db.password")
                                    .orElse("postgres"));

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
