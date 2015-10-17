package io.saso.dash.util

import org.apache.logging.log4j.LogManager
import kotlin.reflect.KClass

fun logger(clazz: KClass<*>) = get(clazz.java)

fun logger(obj: Any) = get(obj.javaClass)

private fun get(clazz: Class<*>) = LogManager.getLogger(clazz)
