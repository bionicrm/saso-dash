package io.saso.dash.util

import java.util.concurrent.Executors

val THREAD_POOL = Executors.newCachedThreadPool()
val SCHEDULING_POOL = Executors.newSingleThreadScheduledExecutor()
