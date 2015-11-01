package io.saso.dash.database.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.saso.dash.database.DBConnector;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.scripts.EntityReturnableSQLScript;
import io.saso.dash.database.scripts.SQLScript;
import io.saso.dash.util.Resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class DashDBFetcher implements DBFetcher
{
    private static final Map<Class<? extends SQLScript>, String> sqlCache =
            new HashMap<>();

    private final DBConnector connector;
    private final Injector injector;

    @Inject
    public DashDBFetcher(DBConnector connector, Injector injector)
    {
        this.connector = connector;
        this.injector = injector;
    }

    @Override
    public <T extends DBEntity> Optional<T> fetch(
            EntityReturnableSQLScript script)
    {
        String sql = sqlCache.computeIfAbsent(script.getClass(), clazz ->
                Resources.get(script.getPath()));

        // noinspection unchecked
        T entity = injector.getInstance((Class<T>) script.getEntityClass());

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            script.setParametersOn(statement);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                entity.fill(resultSet);
                return Optional.of(entity);
            }
            else {
                return Optional.empty();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
