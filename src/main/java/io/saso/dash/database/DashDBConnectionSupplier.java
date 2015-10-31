package io.saso.dash.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.ConfigModel;
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
public class DashDBConnectionSupplier implements DBConnectionSupplier
{
    private static final String DRIVER_URL = "jdbc:apache:commons:dbcp:";
    private static final String POOL_NAME = "saso";

    private static final Logger logger = LogManager.getLogger();

    private final ConfigModel config;

    private GenericObjectPool<PoolableConnection> pool;

    @Inject
    public DashDBConnectionSupplier(ConfigModel config)
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
        return getPool().borrowObject();
    }

    /**
     * Gets the connection pool. Sets {@link #pool}.
     *
     * @return the connection pool
     *
     * @throws SQLException
     */
    private synchronized GenericObjectPool<PoolableConnection> getPool()
            throws SQLException
    {
        if (pool == null) {
            String url = String.format("jdbc:postgresql://%s:%d/%s",
                    config.db.host, config.db.port, config.db.database);

            DriverManagerConnectionFactory connFactory =
                    new DriverManagerConnectionFactory(url, config.db.user, config.db.password);

            PoolableConnectionFactory poolableConnFactory =
                    new PoolableConnectionFactory(connFactory, null);

            pool = new GenericObjectPool<>(poolableConnFactory);

            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(DRIVER_URL);

            poolableConnFactory.setPool(pool);
            driver.registerPool(POOL_NAME, pool);
        }

        return pool;
    }
}
