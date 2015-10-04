package io.saso.dash.server;

import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.saso.dash.auth.Authenticator;
import io.saso.dash.auth.LiveToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DashServerHandler extends ServerHandler
{
    private static final Logger logger = LogManager.getLogger();

    private final Authenticator authenticator;

    private WebSocketServerHandshaker handshaker;

    @Inject
    public DashServerHandler(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }

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
            sendError(ctx, req, HttpResponseStatus.BAD_REQUEST);
        }
        // only GET allowed
        else if (req.method() != HttpMethod.GET) {
            sendError(ctx, req, HttpResponseStatus.FORBIDDEN);
        }
        // handshake
        else {
            final List<String> cookies =
                    req.headers().getAllAndConvert(HttpHeaderNames.COOKIE);
            final Iterator<String> itr = cookies.iterator();
            String liveTokenCookieEncoded = "";

            while (itr.hasNext()) {
                final String s = itr.next();

                if (s.startsWith("live_token=")) {
                    liveTokenCookieEncoded = s;
                    break;
                }
            }

            if (liveTokenCookieEncoded.isEmpty()) {
                sendError(ctx, req, HttpResponseStatus.FORBIDDEN);
            }
            else
            {
                final String liveToken;

                try {
                    liveToken = URLDecoder.decode(
                            ServerCookieDecoder.decode(
                                    liveTokenCookieEncoded).toString(),
                            "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                    sendError(ctx, req, HttpResponseStatus.FORBIDDEN);
                    return;
                }

                logger.debug("liveToken = {}", liveToken);

                final String liveTokenValue =
                        liveToken.substring(
                                "[live_token=".length(),
                                liveToken.length() - 1);

                logger.debug("liveTokenValue = {}", liveTokenValue);

                final Optional<LiveToken> dbLiveToken =
                        authenticator.getLiveToken(liveTokenValue);

                if (! dbLiveToken.isPresent()) {
                    sendError(ctx, req, HttpResponseStatus.FORBIDDEN);
                }

                // TODO: do something with dbLiveToken

                final WebSocketServerHandshakerFactory wsFactory =
                        new WebSocketServerHandshakerFactory(
                                getWebSocketLocation(req), null, true);

                handshaker = wsFactory.newHandshaker(req);

                if (handshaker == null) {
                    WebSocketServerHandshakerFactory
                            .sendUnsupportedVersionResponse(ctx.channel());
                }
                else {
                    handshaker.handshake(ctx.channel(), req);
                }
            }
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest req, FullHttpResponse res)
    {
        final boolean notOk = res.status().code() != 200;

        // generate error page if status isn't OK
        if (notOk) {
            final ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);

            res.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
        }

        // send res and close connection if needed
        final ChannelFuture f = ctx.channel().writeAndFlush(res);

        if (! HttpHeaderUtil.isKeepAlive(req) || notOk) {
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

    private void sendError(ChannelHandlerContext ctx, FullHttpRequest req,
                           HttpResponseStatus status)
    {
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
    }

    private String getWebSocketLocation(FullHttpRequest req)
    {
        return "ws://ws.saso.dev";
    }
}
