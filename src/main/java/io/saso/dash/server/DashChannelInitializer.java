package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.saso.dash.server.pipeline.handlers.RequestValidationHandler;
import io.saso.dash.server.pipeline.handlers.UpgradingHandler;

public class DashChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Provider<RequestValidationHandler> requestValidation;
    private final Provider<UpgradingHandler> upgrading;

    @Inject
    public DashChannelInitializer(Provider<RequestValidationHandler> requestValidation,
                                  Provider<UpgradingHandler> upgrading)
    {
        this.requestValidation = requestValidation;
        this.upgrading = upgrading;
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(requestValidation.get());
        p.addLast(upgrading.get());
    }
}
