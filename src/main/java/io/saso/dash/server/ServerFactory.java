package io.saso.dash.server;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface ServerFactory
{
    ServerWSHandler createWSHandler(WebSocketServerHandshaker handshaker);
}
