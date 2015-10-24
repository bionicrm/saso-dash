package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.Injector

class DashDBEntityFetcher implements DBEntityFetcher
{
    private final Database db
    private final Injector injector

    @Inject
    def DashDBEntityFetcher(Database db, Injector injector)
    {
        this.db = db
        this.injector = injector
    }

    @Override
    <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String sql, ...params)
    {
        final entity = injector.getInstance(entityClass)
        final connection = db.connection
        final statement = connection.prepareStatement(sql)
        def resultSet = null

        try {
            for (i in params.indices) {
                statement.setObject(i + 1, params[i])
            }

            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                entity.fill(resultSet)

                return Optional.of(entity)
            }
            else {
                return Optional.empty()
            }
        }
        finally {
            resultSet?.close()
            statement.close()
            connection.close()
        }
    }
}
