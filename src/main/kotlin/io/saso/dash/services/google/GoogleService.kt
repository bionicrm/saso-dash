package io.saso.dash.services.google

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.services.Service

public class GoogleService : Service()
{
    public override val pollInterval = 30

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {

    }

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {

    }

    override fun stop(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {

    }
}
