package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

public interface ServicePollable
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
    void start(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Polls this service. This method may block. Typically, any new information
     * from within the last poll interval will be sent back to the client from
     * here. This is called every so many seconds, defined by the value of
     * {@link IService#getPollInterval()}.
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
    void stop(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;
}
