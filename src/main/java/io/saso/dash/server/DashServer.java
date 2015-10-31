package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.config.Config;
import io.saso.dash.server.pipeline.handlers.RequestMethodHandler;
import io.saso.dash.server.pipeline.handlers.RequestValidationHandler;
import io.saso.dash.server.pipeline.handlers.UpgradingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    @Inject
    public DashServer(Config config)
    {
        this.config = config;
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

            ChannelFuture f = b.bind(
                    config.<String>get("server.bind.host").orElse("127.0.0.1"),
                    config.<Integer>get("server.bind.port").orElse(80));
            Channel ch = f.sync().channel();

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
        private final ChannelHandler requestValidationHandler =
                new RequestValidationHandler();
        private final ChannelHandler requestMethodHandler =
                new RequestMethodHandler();
        private final ChannelHandler upgradingHandler = new UpgradingHandler();

        @Override
        protected void initChannel(SocketChannel ch)
        {
            ChannelPipeline p = ch.pipeline();

            p.addLast(new HttpServerCodec());
            p.addLast(new HttpObjectAggregator(65536));
            p.addLast(requestValidationHandler);
            p.addLast(requestMethodHandler);
            p.addLast(upgradingHandler);
        }
    }
}
