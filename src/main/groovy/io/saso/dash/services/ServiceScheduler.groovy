package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.entities.LiveToken

interface ServiceScheduler
{
    void schedule(ChannelHandlerContext ctx, LiveToken liveToken)

    void cancel(ChannelHandlerContext ctx, LiveToken liveToken)
}
