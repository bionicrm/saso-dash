package io.saso.dash.server

import com.google.inject.Provider
import com.google.inject.name.Named
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec

class DashChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Provider<Set<ChannelHandler>> httpChannelHandlersProvider

    DashChannelInitializer(
            @Named('http handlers') Provider<Set<ChannelHandler>>
                    httpChannelHandlersProvider)
    {
        this.httpChannelHandlersProvider = httpChannelHandlersProvider
    }

    @Override
    void initChannel(SocketChannel ch)
    {
        final ChannelPipeline pipeline = ch.pipeline()

        pipeline.addLast(new HttpServerCodec())
        pipeline.addLast(new HttpObjectAggregator(65536))

        httpChannelHandlersProvider.get().each { pipeline.addLast(it) }
    }
}
