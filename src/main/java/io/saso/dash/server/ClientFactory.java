package io.saso.dash.server;

import io.netty.channel.ChannelHandlerContext;

public interface ClientFactory
{
    Client create(ChannelHandlerContext ctx);
}
