package io.saso.dash.redis.databases

public interface RedisConnections
{
    fun addIfAllowed(userId: Int): Boolean

    fun remove(userId: Int)
}
