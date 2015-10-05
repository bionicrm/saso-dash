package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServerInitializer extends ServerInitializer
{
    private static final Logger logger = LogManager.getLogger();

    private final Provider<ServerHandler> serverHandlerProvider;

    @Inject
    public DashServerInitializer(Provider<ServerHandler> serverHandlerProvider)
    {
        this.serverHandlerProvider = serverHandlerProvider;

        logger.trace("DashServerInitializer::new");
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        logger.trace("DashServerInitializer#initChannel");

        final ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("handler", serverHandlerProvider.get());
    }
}
