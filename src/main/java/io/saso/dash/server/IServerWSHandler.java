package io.saso.dash.server;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface IServerWSHandler
{
    void setHandshaker(WebSocketServerHandshaker handshaker);
}
