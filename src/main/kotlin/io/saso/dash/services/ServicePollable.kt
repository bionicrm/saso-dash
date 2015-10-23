package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.EntityProvider

public interface ServicePollable
{
    fun start(ctx: ChannelHandlerContext, db: EntityProvider)

    fun poll(ctx: ChannelHandlerContext, db: EntityProvider)

    fun stop(ctx: ChannelHandlerContext)
}
