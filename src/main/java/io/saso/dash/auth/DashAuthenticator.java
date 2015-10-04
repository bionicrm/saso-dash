package io.saso.dash.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.saso.dash.database.DB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class DashAuthenticator implements Authenticator
{
    private static final Logger logger = LogManager.getLogger();

    private final Provider<LiveToken> liveTokenProvider;
    private final Connection db;

    @Inject
    public DashAuthenticator(Provider<LiveToken> liveTokenProvider,
                             @DB Connection db)
    {
        this.liveTokenProvider = liveTokenProvider;
        this.db = db;
    }

    @Override
    public Optional<LiveToken> getLiveToken(String token)
    {
        try {
            final LiveToken liveToken = liveTokenProvider.get();
            final String sql =
                    "SELECT * FROM live_tokens WHERE token = ? LIMIT 1";
            final PreparedStatement statement = db.prepareStatement(sql);

            statement.setString(1, token);

            logger.debug("Execute: {}", sql);

            if (liveToken.fillFromDatabase(statement.executeQuery())) {
                return Optional.of(liveToken);
            }
            else {
                return Optional.empty();
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
