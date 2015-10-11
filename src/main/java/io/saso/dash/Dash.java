package io.saso.dash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.saso.dash.modules.*;
import io.saso.dash.server.Server;

public class Dash
{
    public static void main(String[] args) throws Exception
    {
        final Injector injector = Guice.createInjector(
                new AuthModule(),
                new ConfigModule(),
                new DatabaseModule(),
                new ServerModule(),
                new ServiceModule()
        );

        injector.getInstance(Server.class).start();
    }
}
