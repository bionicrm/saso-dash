package io.saso.dash.database.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.saso.dash.database.DBConnector;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.DBFetcher;
import io.saso.dash.database.DBScriptRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Singleton
public class DashDBFetcher implements DBFetcher
{
    private static final Logger logger = LogManager.getLogger();

    private final DBConnector connector;
    private final DBScriptRepository scriptRepository;
    private final Injector injector;

    @Inject
    public DashDBFetcher(DBConnector connector,
                         DBScriptRepository scriptRepository, Injector injector)
    {
        this.connector = connector;
        this.scriptRepository = scriptRepository;
        this.injector = injector;
    }

    @Override
    public <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String scriptName, Object... params)
    {
        T entity = injector.getInstance(entityClass);
        String sql = scriptRepository.getSQL(scriptName);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(sql)) {
            // set SQL parameters
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                entity.fill(resultSet);
                return Optional.of(entity);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
