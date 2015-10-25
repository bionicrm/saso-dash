package io.saso.dash.redis

import redis.clients.jedis.Jedis

interface Redis
{
    Jedis getConnection(RedisDatabase db)

    def <T> T use(RedisDatabase db, Closure<T> closure)
}
