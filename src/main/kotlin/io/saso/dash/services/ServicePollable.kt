package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider

public interface ServicePollable
{
    fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)

    fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider)

    fun stop(ctx: ChannelHandlerContext)
}
