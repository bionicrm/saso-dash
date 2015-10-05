package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServerInitializer extends ServerInitializer
{
    private static final Logger logger = LogManager.getLogger();

    private final String url;
    private final Provider<ServerHttpHandler> handlerProvider;

    @Inject
    public DashServerInitializer(Config config,
            Provider<ServerHttpHandler> handlerProvider)
    {
        this.handlerProvider = handlerProvider;
        url = config.getString("server.url", "ws://127.0.0.1");
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
