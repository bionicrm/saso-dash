package io.saso.dash.redis.databases;

public enum RedisDatabase
{
    PREFERENCES(0),
    CONCURRENT_CONNECTIONS(1);

    private final int index;

    RedisDatabase(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }
}
