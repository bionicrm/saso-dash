package io.saso.dash.redis.databases

public interface RedisServices
{
    fun set(userId: Int, service: String, data: (String) -> String)

    fun get(userId: Int, service: String): String

    fun remove(userId: Int, service: String)
}
