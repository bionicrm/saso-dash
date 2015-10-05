package io.saso.dash.client;

import io.netty.channel.ChannelHandlerContext;

public interface Client
{
    void onFrame(String msg);

    void onClose();

    ChannelHandlerContext getContext();
}
