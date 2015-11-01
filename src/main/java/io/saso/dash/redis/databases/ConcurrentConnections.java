package io.saso.dash.redis.databases;

public interface ConcurrentConnections
{
    boolean incrementIfAllowed(int userId);

    void decrement(int userId);
}
