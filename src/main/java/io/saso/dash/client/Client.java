package io.saso.dash.client;

import io.netty.channel.ChannelHandlerContext;

public interface Client
{
    void onFrame(ChannelHandlerContext ctx, String msg);

    void onClose(ChannelHandlerContext ctx);
}
