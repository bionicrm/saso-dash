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
import java.util.Properties;

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

    private synchronized void initialize()
    {
        if (connection.isPresent()) return;

        try {
            // load driver
            Class.forName("org.postgresql.Driver");

            final String host     = config.getString("db.host", "127.0.0.1");
            final int port        = config.getInteger("db.port", 5432);
            final String database = config.getString("db.database", "postgres");
            final String user     = config.getString("db.user", "postgres");
            final String password = config.getString("db.password", "");

            final String url = String.format(
                    "jdbc:postgresql://%s:%d/%s", host, port, database);
            final Properties info = new Properties();

            info.setProperty("user", user);
            //info.setProperty("ssl", "true");

            if (! password.isEmpty()) {
                info.setProperty("password", password);
            }

            connection = Optional.of(DriverManager.getConnection(url, info));

            logger.info("Connected to DB @ {}", url);
        }
        catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        catch (SQLException e) {
            logger.error("SQLException: {}", e.getMessage());
            logger.error("SQLState: {}", e.getSQLState());
            logger.error("VendorError: {}", e.getErrorCode());
        }
    }
}
