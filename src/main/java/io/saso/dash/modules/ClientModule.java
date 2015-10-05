package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import io.saso.dash.auth.Authenticator;
import io.saso.dash.auth.DashAuthenticator;
import io.saso.dash.client.Client;
import io.saso.dash.client.ClientFactory;
import io.saso.dash.client.DashClient;

public class ClientModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        install(new FactoryModuleBuilder()
                .implement(Client.class, DashClient.class)
                .build(ClientFactory.class));
    }
}
