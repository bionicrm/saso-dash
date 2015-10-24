package io.saso.dash.database

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import org.apache.commons.dbcp2.DriverManagerConnectionFactory
import org.apache.commons.dbcp2.PoolableConnection
import org.apache.commons.dbcp2.PoolableConnectionFactory
import org.apache.commons.dbcp2.PoolingDriver
import org.apache.commons.pool2.impl.GenericObjectPool

import java.sql.Connection
import java.sql.DriverManager

@Singleton
class DashDatabase implements Database
{
    private static final String DRIVER_URL = 'jdbc:apache:commons:dbcp:'
    private static final String POOL_NAME = 'saso'

    private final Config config

    private final
    Closure<GenericObjectPool<PoolableConnection>> connectionPool = {
        final String host     = config.get('db.host', '127.0.0.1')
        final int    port     = config.get('db.port', 5432)
        final String database = config.get('db.database', 'postgres')
        final String user     = config.get('db.user', 'postgres')
        final String password = config.get('db.password', '')
        final String url      = "jdbc:postgresql://$host:$port/$database"

        final DriverManagerConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(url, user, password)
        final PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null)
        final GenericObjectPool connectionPool =
                new GenericObjectPool(poolableConnectionFactory)
        final PoolingDriver poolingDriver =
                DriverManager.getDriver(DRIVER_URL) as PoolingDriver

        poolableConnectionFactory.pool = connectionPool
        poolingDriver.registerPool(POOL_NAME, connectionPool)

        return connectionPool
    }.memoize()
    
    @Inject
    DashDatabase(Config config)
    {
        this.config = config
    }

    static
    {
        // load drivers
        Class.forName('org.apache.commons.dbcp2.PoolingDriver')
        Class.forName('org.postgresql.Driver')
    }
    
    @Override
    Connection getConnection() {
        return connectionPool().borrowObject()
    }
}
