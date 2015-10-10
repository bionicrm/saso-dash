package io.saso.dash.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.saso.dash.database.DB;
import io.saso.dash.util.LoggingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class DashAuthenticator implements Authenticator
{
    private static final Logger logger = LogManager.getLogger();

    private final Connection db;
    private final Provider<LiveToken> liveTokenProvider;

    @Inject
    public DashAuthenticator(@DB Connection db,
                             Provider<LiveToken> liveTokenProvider)
    {
        this.db = db;
        this.liveTokenProvider = liveTokenProvider;
    }

    @Override
    public Optional<LiveToken> findValidLiveToken(String token)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            final String sql =
                    "SELECT * FROM live_tokens WHERE token = ? LIMIT 1";

            statement = db.prepareStatement(sql);

            statement.setString(1, token);
            logger.info("Execute on DB: {}", sql);

            resultSet = statement.executeQuery();

            // check existence
            if (resultSet.next()) {
                final LiveToken liveToken = liveTokenProvider.get();
                final boolean fillSuccess =
                        liveToken.fillFromResultSet(resultSet);
                final long expiresAtTime = liveToken.getExpiresAt().getTime();
                final long currentTime = System.currentTimeMillis();

                // check for fill success and not expired
                if (fillSuccess && currentTime < expiresAtTime) {
                    return Optional.of(liveToken);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            try {
                if (resultSet != null) statement.close();
                if (statement != null) statement.close();

                db.close();
            }
            catch (SQLException e) {
                LoggingUtil.logSQLException(e, logger);
            }
        }

        return Optional.empty();
    }
}
