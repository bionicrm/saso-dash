package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.database.entities.LiveToken;

public interface ServiceManager
{
    void start(ChannelHandlerContext ctx, LiveToken liveToken) throws Exception;

    void stop(ChannelHandlerContext ctx, LiveToken liveToken) throws Exception;
}
