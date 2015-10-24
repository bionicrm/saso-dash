package io.saso.dash.database

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.config.Config
import org.apache.commons.dbcp2.DriverManagerConnectionFactory
import org.apache.commons.dbcp2.PoolableConnectionFactory
import org.apache.commons.dbcp2.PoolingDriver
import org.apache.commons.pool2.impl.GenericObjectPool
import java.sql.Connection
import java.sql.DriverManager

@Singleton
public class DashDatabaseOLD
@Inject
constructor(private val config: Config) : Database
{
    private val driverUrl = "jdbc:apache:commons:dbcp:"
    private val poolName = "saso"

    private val connectionPool by lazy {
        val host     = config.get("db.host", "127.0.0.1")
        val port     = config.get("db.port", 5432)
        val database = config.get("db.database", "postgres")
        val user     = config.get("db.user", "postgres")
        val password = config.get("db.password", "")
        val url      = "jdbc:postgresql://$host:$port/$database"

        val connectionFactory =
                DriverManagerConnectionFactory(url, user, password)
        val poolableConnectionFactory =
                PoolableConnectionFactory(connectionFactory, null)
        val connectionPool = GenericObjectPool(poolableConnectionFactory)
        val poolingDriver = DriverManager.getDriver(driverUrl)
                as PoolingDriver

        poolableConnectionFactory.pool = connectionPool
        poolingDriver.registerPool(poolName, connectionPool)

        connectionPool
    }

    override val connection: Connection
        get() = connectionPool.borrowObject()

    init
    {
        // load drivers
        Class.forName("org.apache.commons.dbcp2.PoolingDriver")
        Class.forName("org.postgresql.Driver")
    }

}
