package io.saso.dash.client;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.database.DB;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;

public class DashClient implements Client
{
    private final Connection db;
    private final LiveToken liveToken;

    @Inject
    public DashClient(@DB Connection db, @Assisted LiveToken liveToken)
    {
        this.db = db;
        this.liveToken = liveToken;
    }

    @Override
    public void onFrame(ChannelHandlerContext ctx, String msg)
    {
        ctx.channel().writeAndFlush(new TextWebSocketFrame("onFrame!"));
    }

    @Override
    public void onClose(ChannelHandlerContext ctx)
    {

    }
}
