package io.saso.dash.server

import com.google.inject.Inject
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.saso.dash.config.Config
import io.saso.dash.util.logger

public class DashServer
@Inject
constructor(val serverInitializer: ServerInitializer,
            val config: Config): Server
{
    override fun start()
    {
        val bossGroup = EpollEventLoopGroup(1)
        val workerGroup = EpollEventLoopGroup()

        try {
            val b = ServerBootstrap()

            b.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel::class.java)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(serverInitializer)

            val chFuture = b.bind(config.get("server.bind.host", "127.0.0.1"),
                    config.get("server.bind.port", 80))

            val ch = chFuture.sync().channel()

            logger(this) info "Server started @ ${ch.localAddress()}"

            ch.closeFuture().sync()
        }
        finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}
