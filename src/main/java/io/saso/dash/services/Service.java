package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Service implements IService
{
    private final List<Pollable> subPollables = new CopyOnWriteArrayList<>();

    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (Pollable subPollable : subPollables) {
            subPollable.start(ctx, db);
        }

        ctx.channel().flush();
    }

    @Override
    public void poll(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (Pollable subPollable : subPollables) {
            subPollable.poll(ctx, db);
        }

        ctx.channel().flush();
    }

    @Override
    public void stop(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {
        for (Pollable subPollable : subPollables) {
            subPollable.stop(ctx, db);
        }

        ctx.channel().flush();
    }

    protected final void registerSubPollable(Pollable pollable)
    {
        subPollables.add(pollable);
    }

    protected final void unregisterSubPollable(Pollable pollable)
    {
        subPollables.remove(pollable);
    }
}
