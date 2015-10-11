package io.saso.dash.database;

import com.google.inject.Inject;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Future;

public class DashEntityManager implements EntityManager
{
    private final Database db;

    @Inject
    public DashEntityManager(Database db)
    {
        this.db = db;
    }

    @Override
    public <T extends DBEntity, S extends T> Optional<T> execute(
            Class<S> entityClass, String sql, Object... params) throws Exception
    {
        LogManager.getLogger().entry(entityClass, sql, Arrays.toString(params));

        final T entity = entityClass.newInstance();

        try (Connection conn = db.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            // set params
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            final ResultSet resultSet = statement.executeQuery();

            if (! resultSet.next()) {
                return Optional.empty();
            }

            LoggingUtil.logResultSet(resultSet, getClass());

            entity.fillFromResultSet(resultSet);

            // noinspection unchecked
            return Optional.of(entity);
        }
    }
}
