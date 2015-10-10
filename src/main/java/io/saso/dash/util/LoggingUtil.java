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

    /**
     * Logs a Throwable. If the Throwable is an instance of SQLException, extra
     * information will be logged.
     *
     * @param e the Throwable to log
     * @param clazz the class of the caller
     */
    public static void logThrowable(Throwable e, Class clazz)
    {
        final Logger logger = getLoggerForClass(clazz);

        if (e instanceof SQLException) {
            logSQLException((SQLException) e, logger);
        }

        logger.error(e.getMessage(), e);
    }

    /**
     * Logs a ResultSet.
     *
     * @param resultSet the ResultSet to log
     * @param clazz the class of the caller
     */
    public static void logResultSet(ResultSet resultSet, Class clazz)
    {
        final Logger logger = getLoggerForClass(clazz);

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

    /**
     * Logs extra information about an SQLException.
     *
     * @param e the SQLException to log
     * @param logger the logger of the caller
     */
    private static void logSQLException(SQLException e, Logger logger)
    {
        logger.error("SQLException: {}", e.getMessage());
        logger.error("SQLState: {}", e.getSQLState());
        logger.error("VendorError: {}", e.getErrorCode());
    }

    /**
     * Gets the logger for a class.
     *
     * @param clazz the class to get the logger for
     *
     * @return the logger
     */
    private static Logger getLoggerForClass(Class clazz)
    {
        return LogManager.getLogger(clazz);
    }
}
