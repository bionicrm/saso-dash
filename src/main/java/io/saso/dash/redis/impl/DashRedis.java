package io.saso.dash.redis.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.mazeika.uconfig.Config;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.databases.RedisDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.concurrent.TimeUnit;

@Singleton
public class DashRedis implements Redis
{
    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    private JedisPool connectionPool;

    @Inject
    public DashRedis(Config config)
    {
        this.config = config;
    }

    @Override
    public Jedis getConnection(RedisDatabase db)
    {
        Jedis connection = getConnectionPool().getResource();

        connection.select(db.getIndex());
        return connection;
    }

    /**
     * Gets the connection pool, creating it if necessary.
     *
     * @return the created or existing connection pool
     */
    private synchronized JedisPool getConnectionPool()
    {
        if (connectionPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            String host =
                    config.getOrDefault("redis.host", Protocol.DEFAULT_HOST);
            int port = config.getOrDefault("redis.port", Protocol.DEFAULT_PORT);
            String password = config.getOrDefault("redis.password", "");

            if (password.isEmpty()) {
                connectionPool = new JedisPool(poolConfig, host, port);
            }
            else {
                connectionPool = new JedisPool(poolConfig, host, port,
                        Protocol.DEFAULT_TIMEOUT, password);
            }
        }

        return connectionPool;
    }
}
