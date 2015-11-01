package io.saso.dash.redis;

import io.saso.dash.redis.databases.RedisDatabase;
import redis.clients.jedis.Jedis;

public interface Redis
{
    /**
     * Gets a Redis connection from the connection pool.
     *
     * @param db the database to select upon getting the connection
     *
     * @return a Redis connection
     */
    Jedis getConnection(RedisDatabase db);
}
