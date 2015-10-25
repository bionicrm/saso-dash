package io.saso.dash.modules
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Named
import io.saso.dash.services.DashServiceLocal
import io.saso.dash.services.Service
import io.saso.dash.services.ServiceCreator
import io.saso.dash.services.ServiceLocal

class ServiceModule extends AbstractModule
{
    @Override
    void configure()
    {
        bind ServiceLocal to DashServiceLocal
    }
}
