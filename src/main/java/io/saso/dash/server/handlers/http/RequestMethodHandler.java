package io.saso.dash.server.handlers.http;

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
                                   FullHttpRequest req)
    {
        if (req.method() == HttpMethod.GET) {
            ctx.fireChannelRead(req.retain());
        }
        else {
            ChannelHandlerUtil.respond(ctx,
                    HttpResponseStatus.METHOD_NOT_ALLOWED,
                    HttpHeaderUtil.isKeepAlive(req));
        }
    }
}
