package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.auth.*;

public class AuthModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Authenticator.class).to(DashAuthenticator.class);
    }
}
