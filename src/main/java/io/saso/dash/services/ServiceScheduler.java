package io.saso.dash.services;

import io.saso.dash.server.Client;

public interface ServiceScheduler
{
    /**
     * Schedules services for the given client.
     *
     * @param client the client
     */
    void schedule(Client client);

    /**
     * Cancels all the scheduled services for the given client.
     *
     * @param client the client
     */
    void cancel(Client client);
}
