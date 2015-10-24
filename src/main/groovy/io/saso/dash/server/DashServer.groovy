package io.saso.dash.server

import com.google.inject.Inject
import com.google.inject.name.Named
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.saso.dash.config.Config

class DashServer implements Server
{
    private final Config config
    private final ChannelHandler childHandler

    @Inject
    DashServer(Config config, @Named('ch init') ChannelHandler childHandler)
    {
        this.config = config
        this.childHandler = childHandler
    }

    @Override
    void start()
    {
        final bossGroup = new EpollEventLoopGroup(1)
        final workerGroup = new EpollEventLoopGroup()

        try {
            final bootstrap = new ServerBootstrap()

            bootstrap.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(childHandler)

            final channelFuture = bootstrap.bind(
                    config.get('server.bind.host', '127.0.0.1'), // host
                    config.get('server.bind.port', 80))          // port

            final channel = channelFuture.sync().channel()

            // TODO: log startup

            channel.closeFuture().sync()
        }
        finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}
