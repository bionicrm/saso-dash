package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.Injector

import static org.codehaus.groovy.runtime.IOGroovyMethods.withCloseable

class DashEntityManager implements EntityManager
{
    private final Database db
    private final Injector injector

    @Inject
    def DashEntityManager(Database db, Injector injector)
    {
        this.db = db
        this.injector = injector
    }

    @Override
    <T extends DBEntity> Optional<T> execute(Class<T> entityClass,
                                                 String sql, Object... params)
    {
        final entity = injector.getInstance entityClass
        final connection = db.connection
        final statement = connection.prepareStatement sql

        withCloseable({ connection.close(); statement.close() },
        {
            for (i in params.indices) {
                statement.setObject i + 1, params[i]
            }

            final resultSet = statement.executeQuery()

            withCloseable({ resultSet.close() }, {
                if (resultSet.next()) {
                    entity.fillFromResultSet resultSet

                    Optional.of(entity)
                }
                else {
                    Optional.empty()
                }
            })
        })
    }
}
