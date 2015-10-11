package io.saso.dash.services.google;

import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;
import io.saso.dash.services.Service;

public class GoogleService implements Service
{
    private ChannelHandlerContext ctx;
    private User user;
    private ProviderUser providerUser;
    private AuthToken authToken;

    @Override
    public void start() throws Exception
    {

    }

    @Override
    public void poll() throws Exception
    {

    }

    @Override
    public void stop() throws Exception
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

    @Override
    public void setContext(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public void setProviderUser(ProviderUser providerUser)
    {
        this.providerUser = providerUser;
    }

    @Override
    public void setAuthToken(AuthToken authToken)
    {
        this.authToken = authToken;
    }
}
