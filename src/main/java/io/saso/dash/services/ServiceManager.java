package io.saso.dash.services;

import io.saso.dash.database.entities.LiveToken;

public interface ServiceManager extends ContextInjectable
{
    void start() throws Exception;

    void stop() throws Exception;

    void setLiveToken(LiveToken liveToken);
}
