package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.auth.Authenticator;
import io.saso.dash.auth.DashAuthenticator;

public class AuthModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Authenticator.class).to(DashAuthenticator.class);
    }
}
