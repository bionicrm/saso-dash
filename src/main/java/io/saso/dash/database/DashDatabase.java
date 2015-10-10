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

import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DashDatabase implements Database
{
    private static final String DBCP_CONNECT = "jdbc:apache:commons:dbcp:";
    private static final String DBCP_POOL_NAME = "saso";

    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    private PoolingDriver poolingDriver;
    private ObjectPool<PoolableConnection> connectionPool;

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
            LoggingUtil.logThrowable(e, DashDatabase.class);
        }
    }

    @Override
    public PoolableConnection getConnection() throws SQLException
    {
        if (connectionPool == null) {
            initialize();
        }

        try {
            return connectionPool.borrowObject();
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void closePool() throws SQLException
    {
        if (poolingDriver == null) {
            throw new IllegalStateException("No pool to close");
        }

        poolingDriver.closePool(DBCP_POOL_NAME);
    }

    private synchronized void initialize() throws SQLException
    {
        logger.info("Initializing DB connection pool...");

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

        connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolingDriver = (PoolingDriver) DriverManager.getDriver(DBCP_CONNECT);

        poolableConnectionFactory.setPool(connectionPool);
        poolingDriver.registerPool(DBCP_POOL_NAME, connectionPool);

        logger.info("Initialized DB connection pool");
    }
}
