package io.saso.dash.server

import com.google.inject.Inject
import com.google.inject.Provider
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.saso.dash.server.handlers.ServerHttpHandler

public class DashServerInitializer
@Inject
constructor(val handlerProvider: Provider<ServerHttpHandler>):
        ServerInitializer()
{
    override fun initChannel(ch: SocketChannel)
    {
        val pipeline = ch.pipeline()

        // initial HTTP pipeline
        pipeline.addLast(HttpServerCodec())
        pipeline.addLast("httpaggregator", HttpObjectAggregator(65536))
        pipeline.addLast("httphandler", handlerProvider.get())
    }
}
