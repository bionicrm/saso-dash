package io.saso.dash.database

import java.util.*
import kotlin.reflect.KClass

public interface EntityManagerOLD
{
    fun <T : DBEntity> execute(
            entityClass: Class<T>, sql: String, params: List<Any>):
            Optional<T>

    final fun <T : DBEntity> executeOrFail(
            entityClass: Class<T>, sql: String, params: List<Any>): T =
            execute(entityClass, sql, params).get()
}
