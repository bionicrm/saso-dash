package io.saso.dash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.saso.dash.modules.ConfigModule;
import io.saso.dash.modules.GeneralModule;
import io.saso.dash.modules.ServerModule;

public class Dash
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(
                new ConfigModule(),
                new GeneralModule(),
                new ServerModule());

        // TODO: start server
    }
}
