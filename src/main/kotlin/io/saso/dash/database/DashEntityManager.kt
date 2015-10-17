package io.saso.dash.database

import com.google.inject.Inject
import io.saso.dash.util.tryResources
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

public class DashEntityManager
@Inject constructor(val db: Database): EntityManager
{
    override fun <T : DBEntity> execute(
            entityClass: KClass<T>, sql: String, vararg params: Any): Optional<T>
    {
        val entity = entityClass.primaryConstructor!!.call()

        return tryResources {
            val connection = db.connection.autoClose()
            val statement  = connection.prepareStatement(sql).autoClose()

            // set params for statement
            params.forEachIndexed { i, o ->
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
        }
    }


}
