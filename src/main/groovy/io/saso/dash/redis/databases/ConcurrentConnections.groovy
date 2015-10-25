package io.saso.dash.redis.databases

interface ConcurrentConnections
{
    /**
     * Adds a connection for the user if allowed. If not, nothing will happen.
     *
     * @param userId the ID of the user
     *
     * @return whether the user may have another concurrent connection
     */
    boolean addConnection(int userId)

    /**
     * Removes a connection for the user.
     *
     * @param userId the ID of the user
     */
    void removeConnection(int userId)
}
