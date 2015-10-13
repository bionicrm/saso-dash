package io.saso.dash.services;

import com.google.inject.name.Named;
import io.saso.dash.database.entities.LiveToken;

public interface ServiceFactory
{
    DBEntityProvider createDBEntityProvider(LiveToken liveToken);

    @Named("github")
    Service createGitHubService();

    @Named("google")
    Service createGoogleService();
}
