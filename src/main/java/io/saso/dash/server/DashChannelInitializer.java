package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.saso.dash.server.pipeline.RequestValidationHandler;

public class DashChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Provider<RequestValidationHandler> requestValidation;

    @Inject
    public DashChannelInitializer(Provider<RequestValidationHandler> requestValidation)
    {
        this.requestValidation = requestValidation;
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(requestValidation.get());
    }
}
