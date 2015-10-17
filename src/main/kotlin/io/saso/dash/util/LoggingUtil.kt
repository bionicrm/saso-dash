package io.saso.dash.util

import org.apache.logging.log4j.LogManager

fun logger(forClass: Any) = LogManager.getLogger(forClass)

fun logThrowable(forClass: Any, throwable: Throwable) =
        logger(forClass).error(throwable.getMessage(), throwable)
