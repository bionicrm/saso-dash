package io.saso.dash.util;

import java.util.concurrent.TimeUnit;

public interface Poller
{
    void registerPollable(Pollable pollable, long interval, TimeUnit timeUnit);

    void unregisterPollable(Pollable pollable);
}
