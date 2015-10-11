package io.saso.dash.redis;

import redis.clients.jedis.Jedis;

public interface Redis
{
    Jedis getConnection(RedisDatabase db);

    void closePool();
}
