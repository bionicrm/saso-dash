package io.saso.dash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.saso.dash.config.Config;
import io.saso.dash.modules.*;
import io.saso.dash.server.Server;
import io.saso.dash.startup.StartupManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dash
{
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args)
    {
        logger.info("Starting...");

        Injector injector = Guice.createInjector(
                new ConfigModule(),
                new DatabaseModule(),
                new RedisModule(),
                new ServerModule(),
                new StartupModule());

        // run startup
        injector.getInstance(StartupManager.class).start();

        // start server
        injector.getInstance(Server.class).start();
    }
}
