package io.saso.dash.util

import java.util.concurrent.Executors

public val threadPool = Executors.newCachedThreadPool()
public val schedulingPool = Executors.newSingleThreadScheduledExecutor()
