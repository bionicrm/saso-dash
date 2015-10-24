package io.saso.dash.server

import com.google.inject.Provider
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.saso.dash.server.handlers.ServerHttpHandler

class DashChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Provider<ServerHttpHandler> handlerProvider

    DashChannelInitializer(Provider<ServerHttpHandler> handlerProvider)
    {
        this.handlerProvider = handlerProvider
    }

    @Override
    void initChannel(SocketChannel ch)
    {
        final pipeline = ch.pipeline()

        pipeline.addLast new HttpServerCodec()
        pipeline.addLast 'httpaggregator', new HttpObjectAggregator(65536)
        pipeline.addLast 'httphandler', handlerProvider.get()
    }
}
