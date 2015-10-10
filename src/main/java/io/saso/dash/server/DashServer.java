package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.saso.dash.config.Config;
import io.saso.dash.util.LoggingUtil;

public class DashServer implements Server
{
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
                    config.get("server.address", "127.0.0.1"),
                    config.get("server.port", 80));

            chFuture.sync().channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            LoggingUtil.logThrowable(e, getClass());
        }
        finally {
            oioEventLoopGroup.shutdownGracefully();
        }
    }
}
