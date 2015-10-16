package io.saso.dash.server;

import com.google.inject.Inject;
import io.saso.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServer implements Server
{
    private static final Logger LOGGER = LogManager.getLogger();

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
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(serverInitializer);

            final ChannelFuture chFuture = b.bind(
                    config.get("server.bind.host", "127.0.0.1"),
                    config.get("server.bind.port", 80));

            final Channel ch = chFuture.sync().channel();

            LOGGER.info("Server started @ {}", ch.localAddress());

            ch.closeFuture().sync();
        }
        catch (InterruptedException e) {
            LoggingUtil.logThrowable(e, getClass());
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
