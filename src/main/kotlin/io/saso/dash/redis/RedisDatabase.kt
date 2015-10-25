package io.saso.dash.redis

public enum class RedisDatabase
{
    CONCURRENT_CONNECTIONS(0),
    SERVICES(1);

    val index: Int

    constructor(index: Int)
    {
        this.index = index
    }
}
