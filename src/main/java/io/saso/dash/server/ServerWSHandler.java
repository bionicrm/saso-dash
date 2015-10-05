package io.saso.dash.server;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public abstract class ServerWSHandler
        extends SimpleChannelInboundHandler<WebSocketFrame>
{ /* empty */ }
