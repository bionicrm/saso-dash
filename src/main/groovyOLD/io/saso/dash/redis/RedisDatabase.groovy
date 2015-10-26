package io.saso.dash.redis

enum RedisDatabase
{
    CONCURRENT_CONNECTIONS(0),
    SERVICES(1)

    final int index

    RedisDatabase(int index)
    {
        this.index = index
    }
}
