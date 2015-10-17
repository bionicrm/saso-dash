package io.saso.dash.database

import java.util.*
import kotlin.reflect.KClass

public interface EntityManager
{
    fun <T : DBEntity> execute(
            entityClass: KClass<T>, sql: String, vararg params: Any):
            Optional<T>

    fun <T : DBEntity> executeOrFail(
            entityClass: KClass<T>, sql: String, vararg params: Any): T =
            execute(entityClass, sql, params).get()
}
