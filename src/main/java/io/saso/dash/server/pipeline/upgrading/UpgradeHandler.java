package io.saso.dash.server.pipeline.upgrading;

import io.netty.channel.ChannelHandlerContext;

public interface UpgradeHandler
{
    void upgrade(ChannelHandlerContext ctx);
}
