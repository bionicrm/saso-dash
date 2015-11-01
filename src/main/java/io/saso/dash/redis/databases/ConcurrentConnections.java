package io.saso.dash.redis.databases;

public interface ConcurrentConnections
{
    void initialize();

    boolean incrementIfAllowed(int userId);

    void decrement(int userId);
}
