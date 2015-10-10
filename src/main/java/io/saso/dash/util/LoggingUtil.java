package io.saso.dash.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class LoggingUtil
{
    private LoggingUtil() { /* empty */ }

    public static void logThrowable(Throwable e, Class loggingClass)
    {
        final Logger logger = getLoggerForClass(loggingClass);

        if (e instanceof SQLException) {
            logSQLException((SQLException) e, logger);
        }

        logger.error(e.getMessage(), e);
    }

    public static void logResultSet(ResultSet resultSet, Class loggingClass)
    {
        final Logger logger = getLoggerForClass(loggingClass);

        final Map<String, String> entries = new HashMap<>();
        final ResultSetMetaData metaData;
        final int count;

        try {
            metaData = resultSet.getMetaData();
            count = metaData.getColumnCount();

            for (int i = 1; i <= count; i++) {
                entries.put(metaData.getColumnName(i), resultSet.getString(i));
            }

            if (count > 0) {
                logger.info("From \"{}\" got: {}", metaData.getTableName(1),
                        entries);
            }
        }
        catch (SQLException e) {
            logSQLException(e, logger);
        }
    }

    private static void logSQLException(SQLException e, Logger logger)
    {
        logger.error("SQLException: {}", e.getMessage());
        logger.error("SQLState: {}", e.getSQLState());
        logger.error("VendorError: {}", e.getErrorCode());
    }

    private static Logger getLoggerForClass(Class loggingClass)
    {
        return LogManager.getLogger(loggingClass);
    }
}
