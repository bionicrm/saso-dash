package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Service implements ServicePollable
{
    private final List<ServicePollable> pollables =
            new CopyOnWriteArrayList<>();

    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (ServicePollable pollable : pollables) {
            pollable.start(ctx, db);
        }

        ctx.channel().flush();
    }

    @Override
    public void poll(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (ServicePollable pollable : pollables) {
            pollable.poll(ctx, db);
        }

        ctx.channel().flush();
    }

    @Override
    public void stop(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (ServicePollable pollable : pollables) {
            pollable.stop(ctx, db);
        }

        ctx.channel().flush();
    }

    /**
     * Gets the number of seconds between polls. This determines how often
     * {@link #poll(ChannelHandlerContext, DBEntityProvider)} will be called.
     * Ideally, this method should return a constant.
     *
     * @return the number of seconds between polls
     */
    public abstract int getPollInterval();

    /**
     * Gets the name of this provider as represented in the name column of the
     * providers table.
     *
     * @return the provider name
     */
    public abstract String getProviderName();

    protected final void registerSubPollable(ServicePollable pollable)
    {
        pollables.add(pollable);
    }

    protected final void unregisterSubPollable(ServicePollable pollable)
    {
        pollables.remove(pollable);
    }
}
