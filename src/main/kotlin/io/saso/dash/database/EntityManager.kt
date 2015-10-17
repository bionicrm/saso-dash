package io.saso.dash.database

import java.util.*
import kotlin.reflect.KClass

public interface EntityManager
{
    fun <T : DBEntity> execute(
            entityClass: KClass<T>, sql: String, params: List<Any>):
            Optional<T>

    fun <T : DBEntity> executeOrFail(
            entityClass: KClass<T>, sql: String, params: List<Any>): T =
            execute(entityClass, sql, params).get()
}
