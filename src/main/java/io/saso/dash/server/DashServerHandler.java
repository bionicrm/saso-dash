package io.saso.dash.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashServerHandler extends ServerHandler
{
    private static final Logger logger = LogManager.getLogger();

    private WebSocketServerHandshaker handshaker;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg)
    {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        else if (msg instanceof WebSocketFrame)
        {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        // bad request
        if (! req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // only GET allowed
        if (req.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        // handshake
        if (req.uri().equals("/")) {
            final WebSocketServerHandshakerFactory wsFactory =
                    new WebSocketServerHandshakerFactory(
                            getWebSocketLocation(req), null, true);

            handshaker = wsFactory.newHandshaker(req);

            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(
                        ctx.channel());
            }
            else {
                handshaker.handshake(ctx.channel(), req);
            }
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest req, FullHttpResponse res)
    {
        // generate error page if status isn't OK
        if (res.status().code() != 200) {
            final ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);

            res.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
        }

        // send res and close connection if needed
        final ChannelFuture f = ctx.channel().writeAndFlush(res);

        if (! HttpHeaderUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame)
    {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
        }
        else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(
                    frame.content().retain()));
        }
        else if (! (frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(frame.getClass().getName()
                    + " frame types not supported");
        }
        else {
            // TODO: send off to handlers
            String request = ((TextWebSocketFrame) frame).text();
            logger.debug("{} received {}", ctx.channel(), request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase()));
        }
    }

    private String getWebSocketLocation(FullHttpRequest req)
    {
        return "ws://" + req.headers().get(HttpHeaderNames.HOST);
    }
}
