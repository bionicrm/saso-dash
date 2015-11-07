package io.saso.dash.server;

import io.netty.channel.Channel;
import io.saso.dash.database.entities.*;
import io.saso.dash.services.services.Service;

public interface Client
{
    /**
     * Gests the auth token from the DB for the given service.
     *
     * @param service the service
     *
     * @return the auth token from the DB
     */
    DBAuthToken authToken(Service service);

    /**
     * Gets the live token of this client.
     *
     * @return the live token
     */
    DBLiveToken liveToken();

    /**
     * Gets the service from the DB for the given service.
     *
     * @param service the service
     *
     * @return the service from the DB
     */
    DBService service(Service service);

    /**
     * Gets the service user from the DB for the given service.
     *
     * @param service the service
     *
     * @return the service user from the DB
     */
    DBServiceUser serviceUser(Service service);

    /**
     * Gets the user of this client from the DB.
     *
     * @return the user from the DB
     */
    DBUser user();

    /**
     * @see Channel#write(Object)
     */
    Client write(String msg);

    /**
     * @see Channel#flush()
     */
    Client flush();
}
