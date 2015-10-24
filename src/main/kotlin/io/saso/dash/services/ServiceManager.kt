package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.entities.DBLiveToken

public interface ServiceManager
{
    fun start(ctx: ChannelHandlerContext, liveToken: DBLiveToken)

    fun stop(ctx: ChannelHandlerContext, liveToken: DBLiveToken)
}
