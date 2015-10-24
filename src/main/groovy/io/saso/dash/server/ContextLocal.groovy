package io.saso.dash.server

import io.netty.channel.ChannelHandlerContext

interface ContextLocal
{
    def <T> T get(ChannelHandlerContext ctx)

    def <T> void add(ChannelHandlerContext ctx, T value)

    def <T> void remove(ChannelHandlerContext ctx)
}
