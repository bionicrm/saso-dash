package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.config.ConfigModel;
import io.saso.dash.server.pipeline.handlers.RequestMethodHandler;
import io.saso.dash.server.pipeline.handlers.RequestValidationHandler;
import io.saso.dash.server.pipeline.handlers.UpgradingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger logger = LogManager.getLogger();

    private final String bindHost;
    private final int bindPort;

    @Inject
    public DashServer(ConfigModel config)
    {
        bindHost = config.server.bind.host;
        bindPort = config.server.bind.port;
    }

    @Override
    public void start()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new Initializer());

            Channel ch = b.bind(bindHost, bindPort).sync().channel();
            logger.info("Server started at {}", ch.localAddress());
            ch.closeFuture().sync();
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private class Initializer extends ChannelInitializer<SocketChannel>
    {
        private final RequestValidationHandler requestValidation =
                new RequestValidationHandler();
        private final RequestMethodHandler requestMethod =
                new RequestMethodHandler();
        private final UpgradingHandler upgrading = new UpgradingHandler();

        @Override
        protected void initChannel(SocketChannel ch)
        {
            ChannelPipeline p = ch.pipeline();

            p.addLast(new HttpServerCodec());
            p.addLast(new HttpObjectAggregator(65536));
            p.addLast(requestValidation);
            p.addLast(requestMethod);
            p.addLast(upgrading);
        }
    }
}
