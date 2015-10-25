package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.Injector

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

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
        final DBEntity entity = injector.getInstance(entityClass)

        Connection connection = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            connection = db.connection
            statement = connection.prepareStatement(sql)

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
            statement?.close()
            connection?.close()
        }
    }
}
