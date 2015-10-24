package io.saso.dash.services

import io.netty.channel.ChannelHandlerContext
import io.saso.dash.database.entities.DBLiveToken

interface ServiceScheduler
{
    void schedule(ChannelHandlerContext ctx, DBLiveToken liveToken)

    void cancel(ChannelHandlerContext ctx, DBLiveToken liveToken)
}
