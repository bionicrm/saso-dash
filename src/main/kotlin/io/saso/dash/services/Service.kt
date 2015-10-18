package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import java.util.concurrent.CopyOnWriteArrayList

public abstract class Service : ServicePollable
{
    private val pollables = CopyOnWriteArrayList<ServicePollable>()

    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        pollables.forEach {
            it.start(ctx, db)
        }

        ctx.channel().flush()
    }

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        pollables.forEach {
            it.poll(ctx, db)
        }

        ctx.channel().flush()
    }

    override fun stop(ctx: ChannelHandlerContext, db: DBEntityProvider)
    {
        pollables.forEach {
            it.stop(ctx, db)
        }

        ctx.channel().flush()
    }

    abstract val providerName: String

    abstract val pollInterval: Int

    protected fun registerSubPollable(pollable: ServicePollable)
    {
        pollables.add(pollable)
    }

    protected fun unregisterSubPollable(pollable: ServicePollable)
    {
        pollables.remove(pollable)
    }
}
