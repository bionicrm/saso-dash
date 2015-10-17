package io.saso.dash.server.handlers;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class ServerHttpHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{ /* empty */ }
