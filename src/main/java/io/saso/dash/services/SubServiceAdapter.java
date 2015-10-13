package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

public abstract class SubServiceAdapter implements ServicePollable
{
    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    { /* empty */ }

    @Override
    public void poll(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    { /* empty */ }

    @Override
    public void stop(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    { /* empty */ }
}
