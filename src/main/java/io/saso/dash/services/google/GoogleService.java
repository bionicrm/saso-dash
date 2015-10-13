package io.saso.dash.services.google;

import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.services.DBEntityProvider;
import io.saso.dash.services.Service;

public class GoogleService extends Service
{
    @Override
    public void start(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {

    }

    @Override
    public void poll(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {

    }

    @Override
    public void stop(ChannelHandlerContext ctx, DBEntityProvider db)
            throws Exception
    {

    }

    @Override
    public int getPollInterval()
    {
        return 30;
    }

    @Override
    public String getProviderName()
    {
        return "google";
    }
}
