package io.saso.dash.client;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.auth.LiveToken;
import io.saso.dash.database.DB;

import java.sql.Connection;

public class DashClient implements Client
{
    private final Connection db;
    private final ChannelHandlerContext context;
    private final LiveToken liveToken;

    @Inject
    public DashClient(@DB Connection db,
                      @Assisted ChannelHandlerContext context,
                      @Assisted LiveToken liveToken)
    {
        this.db = db;
        this.context = context;
        this.liveToken = liveToken;
    }

    @Override
    public void onFrame(String msg)
    {

    }

    @Override
    public void onClose()
    {

    }

    @Override
    public ChannelHandlerContext getContext()
    {
        return context;
    }
}
