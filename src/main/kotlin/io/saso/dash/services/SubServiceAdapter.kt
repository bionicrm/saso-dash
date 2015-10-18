package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext

public abstract class SubServiceAdapter : ServicePollable
{
    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider) { }

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider) { }

    override fun stop(ctx: ChannelHandlerContext, db: DBEntityProvider) { }
}
