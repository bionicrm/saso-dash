package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

public class DashServerInitializer extends ServerInitializer
{
    private final Provider<ServerHandler> serverHandlerProvider;

    @Inject
    public DashServerInitializer(Provider<ServerHandler> serverHandlerProvider)
    {
        this.serverHandlerProvider = serverHandlerProvider;
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        final ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(serverHandlerProvider.get());
    }
}
