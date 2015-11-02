package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class UpgradeHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LogManager.getLogger();

    private final ChannelHandler[] handlers;

    @Inject
    public UpgradeHandler(
            @Named("server ws handlers") ChannelHandler[] handlers)
    {
        this.handlers = handlers;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        ChannelPipeline p = ctx.channel().pipeline();

        // remove pipeline handlers
        while (p.last() != null) {
            p.removeLast();
        }

        // add WS channel handlers
        p.addLast(new WebSocketServerCompressionHandler());
        p.addLast(handlers);

        ctx.fireChannelRead(req.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
