package io.saso.dash.server.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.entities.*;
import io.saso.dash.server.Client;
import io.saso.dash.services.Service;
import io.saso.dash.util.ContextAttr;

public class DashClient implements Client
{
    private final DBFetcher dbFetcher;
    private final Channel ch;
    private final DBLiveToken liveToken;

    @Inject
    public DashClient(DBFetcher dbFetcher, @Assisted ChannelHandlerContext ctx)
    {
        this.dbFetcher = dbFetcher;
        ch = ctx.channel();
        liveToken = ctx.attr(ContextAttr.LIVE_TOKEN).get();
    }

    @Override
    public int getId()
    {
        return liveToken.getUserId();
    }

    @Override
    public DBAuthToken authToken(Service service)
    {
        return dbFetcher.fetch(DBAuthToken.class, "find_auth_token",
                liveToken.getUserId(), service.getName()).get();
    }

    @Override
    public DBLiveToken liveToken()
    {
        return liveToken;
    }

    @Override
    public DBService service(Service service)
    {
        return dbFetcher.fetch(DBService.class, "find_service",
                service.getName()).get();
    }

    @Override
    public DBServiceUser serviceUser(Service service)
    {
        return dbFetcher.fetch(DBServiceUser.class, "find_service_user",
                liveToken.getUserId(), service.getName()).get();
    }

    @Override
    public DBUser user()
    {
        return dbFetcher.fetch(DBUser.class, "find_user",
                liveToken.getUserId()).get();
    }

    @Override
    public Client write(String msg)
    {
        ch.write(new TextWebSocketFrame(msg));
        return this;
    }

    @Override
    public Client flush()
    {
        ch.flush();
        return this;
    }
}
