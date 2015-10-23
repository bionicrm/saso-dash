package io.saso.dash.modules
import com.google.inject.AbstractModule
import io.saso.dash.services.DashServiceScheduler
import io.saso.dash.services.ServiceScheduler

class ServiceModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind ServiceScheduler to DashServiceScheduler
    }
}
