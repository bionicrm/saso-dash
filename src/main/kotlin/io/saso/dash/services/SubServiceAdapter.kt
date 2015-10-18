package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

public abstract class SubServiceAdapter : ServicePollable
{
    override fun start(ctx: ChannelHandlerContext, db: DBEntityProvider) { }

    override fun poll(ctx: ChannelHandlerContext, db: DBEntityProvider) { }

    override fun stop(ctx: ChannelHandlerContext, db: DBEntityProvider) { }

    protected fun write(ctx: ChannelHandlerContext, toWrite: String)
    {
        ctx.channel().write(TextWebSocketFrame(toWrite))
    }
}
