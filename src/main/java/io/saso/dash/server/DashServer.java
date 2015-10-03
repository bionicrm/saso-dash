package io.saso.dash.server;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger logger = LogManager.getLogger();

    private final int port;
    private final ServerInitializer serverInitializer;

    @Inject
    public DashServer(@Named("port") int port,
                      ServerInitializer serverInitializer)
    {
        this.port = port;
        this.serverInitializer = serverInitializer;
    }

    @Override
    public void start()
    {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(serverInitializer);

            final Channel ch = b.bind(port).sync().channel();

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
}
