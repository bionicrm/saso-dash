package io.saso.dash.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadUtil
{
    private ThreadUtil() { }

    public static final ExecutorService CACHED_THREAD_POOL =
            Executors.newCachedThreadPool();
}
