package io.saso.dash.auth;

import com.google.inject.Inject;
import io.saso.dash.database.DB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DashAuthenticator implements Authenticator
{
    private static final Logger logger = LogManager.getLogger();

    private final Connection db;

    @Inject
    public DashAuthenticator(@DB Connection db)
    {
        this.db = db;
    }

    @Override
    public boolean isTokenValid(String token)
    {
        try {
            final String sql =
                    "SELECT expires_at FROM live_tokens WHERE token = ? LIMIT 1";
            final PreparedStatement statement = db.prepareStatement(sql);

            statement.setString(1, token);
            logger.debug("Execute: {}", sql);

            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                final Timestamp expiresAt =
                        resultSet.getTimestamp("expires_at");

                return expiresAt.getTime() > System.currentTimeMillis();
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
}
