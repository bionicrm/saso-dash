package io.saso.dash.server.pipeline.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class UpgradingHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {

    }
}
