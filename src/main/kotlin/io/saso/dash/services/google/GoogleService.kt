package io.saso.dash.services.google

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.EntityProvider
import io.saso.dash.services.Service

public class GoogleService : Service()
{
    public override val pollInterval = 30

    override fun start(ctx: ChannelHandlerContext, db: EntityProvider)
    {

    }

    override fun poll(ctx: ChannelHandlerContext, db: EntityProvider)
    {

    }

    override fun stop(ctx: ChannelHandlerContext, db: EntityProvider)
    {

    }
}
