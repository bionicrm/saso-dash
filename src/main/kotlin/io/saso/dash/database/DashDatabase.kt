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
public class DashDatabase
@Inject
constructor(val config: Config) : Database
{
    object Driver {
        val url = "jdbc:apache:commons:dbcp:"
    }

    object Pool {
        val name = "saso"
    }

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
        val poolingDriver = DriverManager.getDriver(Driver.url)
                as PoolingDriver

        poolableConnectionFactory.pool = connectionPool
        poolingDriver.registerPool(Pool.name, connectionPool)

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
