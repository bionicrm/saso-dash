package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.saso.dash.database.EntityProvider

public abstract class SubServiceAdapter : ServicePollable
{
    override fun start(ctx: ChannelHandlerContext, db: EntityProvider) { }

    override fun poll(ctx: ChannelHandlerContext, db: EntityProvider) { }

    override fun stop(ctx: ChannelHandlerContext, db: EntityProvider) { }

    protected fun write(ctx: ChannelHandlerContext, toWrite: String)
    {
        ctx.channel().writeAndFlush(TextWebSocketFrame(toWrite))
    }
}
