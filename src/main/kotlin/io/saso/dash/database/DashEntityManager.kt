package io.saso.dash.database

import com.google.inject.Inject
import com.google.inject.Injector
import io.saso.dash.util.logThrowable
import io.saso.dash.util.tryResources
import java.sql.SQLException
import java.util.*
import kotlin.reflect.KClass

public class DashEntityManager
@Inject constructor(val db: Database, val injector: Injector): EntityManager
{
    override fun <T : DBEntity> execute(
            entityClass: KClass<T>, sql: String, vararg params: Any):
            Optional<T>
    {
        val entity = injector.getInstance(entityClass.java)

        return tryResources({
            val connection = db.connection.autoClose()
            val statement  = connection.prepareStatement(sql).autoClose()

            println(params.size())

            // set params for statement
            params.forEachIndexed { i, o ->
                println(" $i. " + o.toString())
                Thread.dumpStack()
                statement.setObject(i + 1, o)
            }

            val resultSet = statement.executeQuery().autoClose()

            if (resultSet.next()) {
                entity.fillFromResultSet(resultSet)

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
