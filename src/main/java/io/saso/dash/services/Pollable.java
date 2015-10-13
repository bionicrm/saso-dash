package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

public interface Pollable
{
    /**
     * Starts this pollable.
     *
     * @throws Exception
     */
    void start(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Polls this pollable.
     *
     * @throws Exception
     */
    void poll(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;

    /**
     * Stops this pollable.
     *
     * @throws Exception
     */
    void stop(ChannelHandlerContext ctx, DBEntityProvider db) throws Exception;
}
