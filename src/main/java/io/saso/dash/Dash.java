package io.saso.dash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.saso.dash.modules.ConfigModule;

public class Dash
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new ConfigModule());

        // TODO: start server
    }
}
