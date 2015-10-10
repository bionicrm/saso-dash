package io.saso.dash.util;

import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class LoggingUtil
{
    private LoggingUtil() { /* empty */ }

    public static void logSQLException(SQLException e, Logger logger)
    {
        logger.error("SQLException: {}", e.getMessage());
        logger.error("SQLState: {}", e.getSQLState());
        logger.error("VendorError: {}", e.getErrorCode());
    }

    public static void logResultSet(ResultSet resultSet, Logger logger)
    {
        final Map<String, String> entries = new HashMap<>();
        final ResultSetMetaData metaData;
        final int count;

        try {
            metaData = resultSet.getMetaData();
            count = metaData.getColumnCount();

            for (int i = 1; i <= count; i++) {
                entries.put(
                        metaData.getColumnName(i),
                        resultSet.getString(i));
            }

            logger.info("From \"{}\" got: {}",
                    resultSet.getMetaData().getTableName(1), entries);
        }
        catch (SQLException e) {
            logSQLException(e, logger);
        }
    }
}
