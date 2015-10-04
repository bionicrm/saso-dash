package io.saso.dash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import io.saso.dash.database.Database;
import io.saso.dash.modules.AuthModule;
import io.saso.dash.modules.ConfigModule;
import io.saso.dash.modules.DatabaseModule;
import io.saso.dash.modules.ServerModule;
import io.saso.dash.server.Server;

public class Dash
{
    public static void main(String[] args) throws Exception
    {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        final Injector injector = Guice.createInjector(
                new AuthModule(),
                new ConfigModule(),
                new DatabaseModule(),
                new ServerModule()
        );

        new Thread(() -> {
            injector.getInstance(Server.class).start();
        }).start();
        
        injector.getInstance(Database.class).getConnection();
    }
}
