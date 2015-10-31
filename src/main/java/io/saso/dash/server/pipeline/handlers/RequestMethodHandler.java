package io.saso.dash.server.pipeline.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.util.ChannelHandlerUtil;

@ChannelHandler.Sharable
public class RequestMethodHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest msg)
    {
        if (msg.method() == HttpMethod.GET) {
            ctx.fireChannelRead(msg.retain());
        }
        else {
            ChannelHandlerUtil.respond(ctx,
                    HttpResponseStatus.METHOD_NOT_ALLOWED,
                    HttpHeaderUtil.isKeepAlive(msg));
        }
    }
}
