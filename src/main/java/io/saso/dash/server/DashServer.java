package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger logger = LogManager.getLogger();

    private final ServerInitializer serverInitializer;
    private final Config config;

    @Inject
    public DashServer(ServerInitializer serverInitializer, Config config)
    {
        this.serverInitializer = serverInitializer;
        this.config = config;
    }

    @Override
    public void start()
    {
        final EventLoopGroup oioEventLoopGroup = new OioEventLoopGroup();

        try {
            final ServerBootstrap b = new ServerBootstrap();

            b.group(oioEventLoopGroup)
                    .channel(OioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(serverInitializer);

            final ChannelFuture chFuture = b.bind(
                    config.getString("server.address", "127.0.0.1"),
                    config.getInteger("server.port", 7692));

            chFuture.sync().channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            oioEventLoopGroup.shutdownGracefully();
        }
    }
}
