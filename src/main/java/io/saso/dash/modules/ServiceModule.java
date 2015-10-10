package io.saso.dash.modules;

import com.google.inject.AbstractModule;
import io.saso.dash.services.*;

public class ServiceModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        // GitHub
        bind(Service.class).annotatedWith(GitHub.class).to(GitHubService.class);

        // Google
        bind(Service.class).annotatedWith(Google.class).to(GoogleService.class);
    }
}
