package io.saso.dash.client;

import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.auth.LiveToken;

public interface ClientFactory
{
    Client createClient(ChannelHandlerContext context, LiveToken liveToken);
}
