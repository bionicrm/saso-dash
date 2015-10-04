package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import io.saso.dash.auth.*;
import io.saso.dash.config.Config;
import io.saso.dash.config.DashConfig;

public class AuthModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Authenticator.class).to(DashAuthenticator.class);

        bind(LiveToken.class).to(DashLiveToken.class);
    }
}
