package io.saso.dash.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.saso.dash.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

@Singleton
public class DashDatabase implements Database
{
    private static final Logger logger = LogManager.getLogger();

    private final Config config;

    private Optional<Connection> connection = Optional.empty();

    @Inject
    public DashDatabase(Config config)
    {
        this.config = config;
    }

    @Override
    public Connection getConnection()
    {
        initialize();

        return connection.get();
    }

    private void initialize()
    {
        if (connection.isPresent()) return;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            final String url = String.format(
                    "jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                    config.getString("db.host", "localhost"),
                    config.getInteger("db.port", 3306),
                    config.getString("db.name", "db"),
                    config.getString("db.user", "root"),
                    config.getString("db.pass", ""));
            final Connection conn = DriverManager.getConnection(url);

            this.connection = Optional.of(conn);
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        catch (SQLException e) {
            logger.error("SQLException: {}", e.getMessage());
            logger.error("SQLState: {}", e.getSQLState());
            logger.error("VendorError: {}", e.getErrorCode());
        }
    }
}
