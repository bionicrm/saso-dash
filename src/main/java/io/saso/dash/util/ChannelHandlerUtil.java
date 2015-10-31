package io.saso.dash.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public final class ChannelHandlerUtil
{
    private ChannelHandlerUtil() { }

    public static void respond(ChannelHandlerContext ctx, HttpResponseStatus status,
                               boolean keepAlive)
    {
        ByteBuf content = Unpooled.copiedBuffer(status.toString(), CharsetUtil.UTF_8);
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);

        ChannelFuture f = ctx.writeAndFlush(response);

        if (! keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
