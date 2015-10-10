package io.saso.dash.client;

import io.saso.dash.auth.LiveToken;

public interface ClientFactory
{
    Client createClient(LiveToken liveToken);
}
