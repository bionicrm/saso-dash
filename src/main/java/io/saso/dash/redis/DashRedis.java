package io.saso.dash.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Singleton
public class DashRedis implements Redis
{
    private final Config config;

    private JedisPool pool;

    @Inject
    public DashRedis(Config config)
    {
        this.config = config;
    }

    @Override
    public Jedis getConnection(RedisDatabase db)
    {
        initialize();

        final Jedis conn = pool.getResource();

        conn.select(db.getIndex());

        return conn;
    }

    @Override
    public void closePool()
    {
        pool.destroy();
    }

    private synchronized void initialize()
    {
        if (pool != null) return;

        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        final String host     = config.get("redis.host", "127.0.0.1");
        final int port        = config.get("redis.port", 6379);
        final String password = config.get("redis.password", "");

        if (password.isEmpty()) {
            pool = new JedisPool(
                    poolConfig, host, port);
        }
        else {
            pool = new JedisPool(
                    poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, password);
        }
    }
}
