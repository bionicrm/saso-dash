package io.saso.dash.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.util.ChannelHandlerUtil;

@ChannelHandler.Sharable
public class RequestValidationHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
    {
        if (msg.decoderResult().isSuccess()) {
            ctx.fireChannelRead(msg.retain());
        }
        else {
            ChannelHandlerUtil.respondAndClose(ctx, HttpResponseStatus.BAD_REQUEST);
        }
    }
}
