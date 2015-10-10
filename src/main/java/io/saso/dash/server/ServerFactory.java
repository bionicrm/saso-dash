package io.saso.dash.server;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.saso.dash.client.Client;

public interface ServerFactory
{
    ServerWSHandler createWSHandler(WebSocketServerHandshaker handshaker,
                                    Client client);
}
