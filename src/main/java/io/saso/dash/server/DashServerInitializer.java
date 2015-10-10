package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class DashServerInitializer extends ServerInitializer
{
    private final Provider<ServerHttpHandler> handlerProvider;

    @Inject
    public DashServerInitializer(Provider<ServerHttpHandler> handlerProvider)
    {
        this.handlerProvider = handlerProvider;
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        final ChannelPipeline pipeline = ch.pipeline();

        // initial http pipeline
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast("httpaggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("httphandler", handlerProvider.get());
    }
}
