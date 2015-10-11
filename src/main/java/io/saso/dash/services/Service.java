package io.saso.dash.services;

import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;

public interface Service extends ContextInjectable
{
    /**
     * Starts this service. This method may block. Typically, any new
     * information from while the user was not connected will be sent back to
     * the client from here.
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Polls this service. This method may block. Typically, any new information
     * from within the last poll interval will be sent back to the client from
     * here. This is called every so many seconds, defined by the value of
     * {@link #getPollInterval()}.
     * <p>
     * The first time this is called is after the
     * poll interval. If polling is desired immediately after the timer is
     * started, simply call this method from {@link #start()}.
     *
     * @throws Exception
     */
    void poll() throws Exception;

    /**
     * Stops this service. This method may block.
     *
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * Gets the number of seconds between polls. This determines how often
     * {@link #poll()} will be called. Ideally, this method should return a
     * constant.
     *
     * @return the number of seconds between polls
     */
    int getPollInterval();

    /**
     * Gets the name of this provider as represented in the name column of the
     * providers table.
     *
     * @return the provider name
     */
    String getProviderName();

    /**
     * Sets the user.
     *
     * @param user the user
     */
    void setUser(User user);

    /**
     * Sets the provider user.
     *
     * @param providerUser the provider user
     */
    void setProviderUser(ProviderUser providerUser);

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token
     */
    void setAuthToken(AuthToken authToken);
}
