package io.saso.dash.modules

import com.google.inject.AbstractModule
import io.saso.dash.auth.Authenticator
import io.saso.dash.auth.DashAuthenticator

class AuthModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind Authenticator to DashAuthenticator
    }
}
