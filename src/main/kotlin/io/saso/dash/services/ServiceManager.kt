package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.entities.LiveToken

public interface ServiceManager
{
    fun start(ctx: ChannelHandlerContext, liveToken: LiveToken)

    fun stop(ctx: ChannelHandlerContext, liveToken: LiveToken)
}
