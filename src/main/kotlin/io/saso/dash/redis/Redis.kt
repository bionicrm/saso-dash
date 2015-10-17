package io.saso.dash.redis

import redis.clients.jedis.Jedis

public interface Redis
{
    fun getConnection(db: RedisDatabase): Jedis
}
