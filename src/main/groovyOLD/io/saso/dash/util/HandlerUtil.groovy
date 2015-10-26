package io.saso.dash.util
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.util.CharsetUtil

final class HandlerUtil
{
    private HandlerUtil() { /* empty */ }

    static void sendResponseAndClose(
            ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        ctx.writeAndFlush(
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, status,
                        Unpooled.copiedBuffer(
                                status.toString(), CharsetUtil.UTF_8)))
                .addListener(ChannelFutureListener.CLOSE)
    }
}
