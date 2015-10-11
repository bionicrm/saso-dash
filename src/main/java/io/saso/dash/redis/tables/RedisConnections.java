package io.saso.dash.redis.tables;

public interface RedisConnections
{
    /**
     * Adds a connection to a user. If the number of concurrent connections for
     * that user exceeds the maximum, the connection will not be added and
     * {@code false} will be returned.
     *
     * @param userId the user's ID
     *
     * @return if the connection is allowed
     */
    boolean addIfAllowed(int userId);

    /**
     * Removes a connection from a user.
     *
     * @param userId the user's ID
     */
    void remove(int userId);
}
