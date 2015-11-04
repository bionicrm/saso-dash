package io.saso.dash.services.services;

import io.saso.dash.server.Client;

public interface Service
{
    /**
     * Gets the name of this service. It <em>must</em> match the name that would
     * be found in the DB.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the poll interval of this service in seconds. By default, returns
     * {@code -1}, indicating that this service should not be polled.
     *
     * @return the poll interval, or {@code -1} if this service should not be
     *         polled
     */
    default int getPollInterval()
    {
        return -1;
    }

    /**
     * Starts the service.
     *
     * @param client the client
     */
    default void start(Client client) { }

    /**
     * Polls the service. Called as per {@link #getPollInterval()}.
     *
     * @param client the client
     */
    default void poll(Client client) { }

    /**
     * Stops the service.
     *
     * @param client the client
     */
    default void stop(Client client) { }
}
