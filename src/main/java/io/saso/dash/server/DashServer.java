package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.binding_annotations.ChInit;
import io.saso.dash.config.ConfigModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger logger = LogManager.getLogger();

    private final ChannelHandler channelInitializer;
    private final String bindHost;
    private final int bindPort;

    @Inject
    public DashServer(@ChInit ChannelHandler channelInitializer, ConfigModel config)
    {
        this.channelInitializer = channelInitializer;
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
                    .childHandler(channelInitializer);

            ChannelFuture f = b.bind(bindHost, bindPort).sync();

            logger.info("Server started at {}", f.channel().localAddress());
            // FIXME: not stopping unless force-killed????
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
