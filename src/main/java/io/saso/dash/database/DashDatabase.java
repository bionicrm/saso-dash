package io.saso.dash.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.Config;
import io.saso.dash.util.LoggingUtil;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Singleton
public class DashDatabase implements Database
{
    private static final String DBCP_CONNECT = "jdbc:apache:commons:dbcp:";
    private static final String DBCP_POOL_NAME = "saso";

    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    private boolean initialized;

    @Inject
    public DashDatabase(Config config)
    {
        this.config = config;
    }

    static {
        // load drivers
        try {
            Class.forName("org.apache.commons.dbcp2.PoolingDriver");
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Connection getConnection()
    {
        initialize();

        try {
            logger.debug("Retrieving DB connection from connection pool");

            return DriverManager.getConnection(
                    DBCP_CONNECT + DBCP_POOL_NAME);
        }
        catch (SQLException e) {
            LoggingUtil.logSQLException(e, logger);
        }

        throw new NoSuchElementException(
                "Unable to retrieve DB connection from connection pool");
    }

    @Override
    public void shutdown()
    {
        final PoolingDriver poolingDriver;

        try {
            poolingDriver =
                    (PoolingDriver) DriverManager.getDriver(DBCP_CONNECT);

            poolingDriver.closePool(DBCP_POOL_NAME);
        }
        catch (SQLException e) {
            LoggingUtil.logSQLException(e, logger);
        }
    }

    private synchronized void initialize()
    {
        if (initialized) return;

        logger.debug("Initializing DB connection pool...");

        final String host     = config.getString("db.host", "127.0.0.1");
        final int port        = config.getInteger("db.port", 5432);
        final String database = config.getString("db.database", "postgres");
        final String user     = config.getString("db.user", "postgres");
        final String password = config.getString("db.password", null);

        final String url = String.format(
                "jdbc:postgresql://%s:%d/%s", host, port, database);

        final ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(url, user, password);

        final PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        final ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);

        poolableConnectionFactory.setPool(connectionPool);

        final PoolingDriver driver;

        try {
            driver = (PoolingDriver) DriverManager.getDriver(DBCP_CONNECT);
        }
        catch (SQLException e) {
            LoggingUtil.logSQLException(e, logger);
            return;
        }

        driver.registerPool(DBCP_POOL_NAME, connectionPool);
        logger.debug("Initialized DB connection pool");

        initialized = true;
    }
}
