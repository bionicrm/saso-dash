package io.saso.dash.database

import com.google.inject.Inject
import com.google.inject.Injector
import io.saso.dash.util.logResultSet
import io.saso.dash.util.logThrowable
import io.saso.dash.util.tryResources
import java.sql.SQLException
import java.util.*
import kotlin.reflect.KClass

public class DashEntityManagerOLD
@Inject
constructor(private val db: Database, private val injector: Injector) :
        EntityFetcher
{
    override fun <T : DBEntity> execute(
            entityClass: Class<T>, sql: String, params: List<Any>):
            Optional<T>
    {
        val entity = injector getInstance entityClass

        return tryResources({
            val connection = db.connection.autoClose()
            val statement  = (connection prepareStatement sql).autoClose()

            // set params for statement
            params.forEachIndexed { i, o ->
                statement.setObject(i + 1, o)
            }

            val resultSet = statement.executeQuery().autoClose()

            if (resultSet.next()) {
                logResultSet(this@DashEntityManager, resultSet)

                entity fill resultSet

                Optional.of(entity)
            }
            else {
                Optional.empty()
            }
        }, { e: SQLException ->
            logThrowable(this@DashEntityManager, e)

            Optional.empty()
        })
    }


}
