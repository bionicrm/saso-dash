package io.saso.dash.redis.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.Config;
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
            long start = System.nanoTime();

            JedisPoolConfig poolConfig = new JedisPoolConfig();
            String host = config.<String>get("redis.host")
                    .orElse(Protocol.DEFAULT_HOST);
            int port = config.<Integer>get("redis.port")
                    .orElse(Protocol.DEFAULT_PORT);
            String password = config.<String>get("redis.password").orElse("");

            if (password.isEmpty()) {
                connectionPool = new JedisPool(poolConfig, host, port);
            }
            else {
                connectionPool = new JedisPool(poolConfig, host, port,
                        Protocol.DEFAULT_TIMEOUT, password);
            }

            long end = System.nanoTime();
            logger.debug("Created Redis connection pool in about {}µs",
                    TimeUnit.NANOSECONDS.toMicros(end - start));
        }

        return connectionPool;
    }
}
