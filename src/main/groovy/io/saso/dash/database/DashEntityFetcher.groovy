package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.Injector

import static org.codehaus.groovy.runtime.IOGroovyMethods.withCloseable as tryAndClose

class DashEntityFetcher implements EntityFetcher
{
    private final Database db
    private final Injector injector

    @Inject
    def DashEntityFetcher(Database db, Injector injector)
    {
        this.db = db
        this.injector = injector
    }

    @Override
    <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String sql, Object... params)
    {
        final entity = injector.getInstance entityClass
        final connection = db.connection
        final statement = connection.prepareStatement sql

        tryAndClose({ connection.close(); statement.close() },
        {
            for (i in params.indices) {
                statement.setObject i + 1, params[i]
            }

            final resultSet = statement.executeQuery()

            tryAndClose({ resultSet.close() },
            {
                if (resultSet.next()) {
                    entity.fill resultSet

                    return Optional.of(entity)
                }
                else {
                    return Optional.empty()
                }
            })
        })
    }
}
