package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

public interface IService extends Pollable
{
    /**
     * Starts this service. This method may block. Typically, any new
     * information from while the user was not connected will be sent back to
     * the client from here.
     * <p>
     * If overridden in a service, {@code super.start(ctx, db)} should be called
     * at the end of the method to ensure propagation of the event to
     * subservices and the flushing of the context.
     *
     * @throws Exception
     */
    @Override
    void start(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Polls this service. This method may block. Typically, any new information
     * from within the last poll interval will be sent back to the client from
     * here. This is called every so many seconds, defined by the value of
     * {@link #getPollInterval()}.
     * <p>
     * The first time this is called is after the
     * poll interval. If polling is desired immediately after the timer is
     * started, simply call this method from
     * {@link #start(ChannelHandlerContext, DBEntityProvider)}.
     * <p>
     * If overridden in a service, {@code super.poll(ctx, db)} should be called
     * at the end of the method to ensure propagation of the event to
     * subservices and the flushing of the context.
     *
     * @throws Exception
     */
    @Override
    void poll(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Stops this service. This method may block.
     * <p>
     * If overridden in a service, {@code super.stop(ctx, db)} should be called
     * at the end of the method to ensure propagation of the event to
     * subservices and the flushing of the context.
     *
     * @throws Exception
     */
    @Override
    void stop(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Gets the number of seconds between polls. This determines how often
     * {@link #poll(ChannelHandlerContext, DBEntityProvider)} will be called.
     * Ideally, this method should return a constant.
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
}
