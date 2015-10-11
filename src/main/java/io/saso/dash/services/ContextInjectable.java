package io.saso.dash.services;

import io.netty.channel.ChannelHandlerContext;

public interface ContextInjectable
{
    void setContext(ChannelHandlerContext ctx);
}
