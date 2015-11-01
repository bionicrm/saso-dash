package io.saso.dash.server.handlers.http;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.saso.dash.server.CookieFinder;
import io.saso.dash.util.ChannelHandlerAttr;
import io.saso.dash.util.ChannelHandlerUtil;

import java.util.Optional;

@ChannelHandler.Sharable
public class LiveTokenCookieHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private final CookieFinder cookieFinder;

    @Inject
    public LiveTokenCookieHandler(CookieFinder cookieFinder)
    {
        this.cookieFinder = cookieFinder;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest req)
    {
        Optional<String> tokenOptional =
                cookieFinder.find("live_token", req.headers());

        if (tokenOptional.isPresent()) {
            ctx.attr(ChannelHandlerAttr.TOKEN_COOKIE_VALUE).set(
                    tokenOptional.get());
            ctx.fireChannelRead(req.retain());
        }
        else {
            ChannelHandlerUtil.respond(ctx, HttpResponseStatus.FORBIDDEN,
                    HttpHeaderUtil.isKeepAlive(req));
        }
    }
}
