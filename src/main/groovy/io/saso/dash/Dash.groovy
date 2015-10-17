package io.saso.dash
import com.google.inject.Guice
import io.saso.dash.events.ShutdownManager
import io.saso.dash.modules.*
import io.saso.dash.server.Server

class Dash
{
    static void main(String... args)
    {
        final injector = Guice.createInjector new AuthModule(),
                new ConfigModule(),
                new DatabaseModule(),
                new RedisModule(),
                new ServerModule(),
                new ServiceModule(),
                new TemplatingModule()

        injector.getInstance Server start()
    }
}
