package io.saso.dash.redis.databases;

public interface ConcurrentConnections
{
    /**
     * Initializes the concurrent connections Redis DB.
     */
    void initialize();

    /**
     * Increments the number of concurrent connections for the given user ID if
     * allowed. If not allowed, {@code false} will be returned and the number
     * of concurrent connections for the user ID will remain unchanged.
     *
     * @param userId the user ID to increment for
     *
     * @return {@code true} if the increment for the user ID is allowed
     */
    boolean incrementIfAllowed(int userId);

    /**
     * Decrements the number of concurrent connections for the given user ID.
     *
     * @param userId the user ID to decrement for
     */
    void decrement(int userId);
}
