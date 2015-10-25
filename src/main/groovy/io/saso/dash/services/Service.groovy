package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider

trait Service
{
    final int pollTime = -1

    abstract void start(ChannelHandlerContext ctx,
                        DBEntityProvider entityProvider)

    abstract void poll(ChannelHandlerContext ctx,
                       DBEntityProvider entityProvider)

    abstract void stop(ChannelHandlerContext ctx,
                       DBEntityProvider entityProvider)
}
