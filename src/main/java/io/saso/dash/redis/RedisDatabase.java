package io.saso.dash.redis;

public enum RedisDatabase
{
    CONCURRENT_CONNECTIONS(0),
    ;

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
