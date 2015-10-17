package io.saso.dash.server.handlers;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface IServerWSHandler
{
    void setHandshaker(WebSocketServerHandshaker handshaker);
}
