package io.saso.dash

import com.google.inject.Guice
import io.saso.dash.modules.AuthModule
import io.saso.dash.modules.ConfigModule
import io.saso.dash.modules.DatabaseModule
import io.saso.dash.modules.RedisModule
import io.saso.dash.modules.ServerModule
import io.saso.dash.modules.ServiceModule
import io.saso.dash.modules.TemplatingModule
import io.saso.dash.server.Server
import io.saso.dash.startup.StartupPipeline

final injector = Guice.createInjector new AuthModule(),
        new ConfigModule(),
        new DatabaseModule(),
        new RedisModule(),
        new ServerModule(),
        new ServiceModule(),
        new TemplatingModule()

// startup pipeline
injector.getInstance(StartupPipeline).run()

// start server
injector.getInstance(Server).start()
