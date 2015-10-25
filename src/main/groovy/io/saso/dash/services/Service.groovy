package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider

trait Service
{
    final int pollTime = -1

    abstract ServiceName name

    final Class<Service>[] subServices = []

    void start(ChannelHandlerContext ctx, DBEntityProvider db) { /* empty */ }

    void poll(ChannelHandlerContext ctx, DBEntityProvider db) { /* empty */ }

    void stop(ChannelHandlerContext ctx, DBEntityProvider db) { /* empty */ }
}
