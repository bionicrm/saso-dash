package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.services.*;
import io.saso.dash.util.DashPoller;
import io.saso.dash.util.Poller;

public class UtilModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Poller.class).to(DashPoller.class);
    }
}
