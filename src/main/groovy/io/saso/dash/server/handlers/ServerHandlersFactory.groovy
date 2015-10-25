package io.saso.dash.server.handlers

import com.google.inject.name.Named
import io.netty.channel.ChannelHandler
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker

interface ServerHandlersFactory
{
    @Named('ws handler')
    ChannelHandler createWSHandler(WebSocketServerHandshaker handshaker)
}
