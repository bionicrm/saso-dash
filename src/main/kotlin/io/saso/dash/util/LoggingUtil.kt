package io.saso.dash.util

import org.apache.logging.log4j.LogManager
import java.sql.ResultSet

fun logger(forClass: Any) = LogManager.getLogger(forClass)

fun logThrowable(forClass: Any, throwable: Throwable) =
        logger(forClass).error(throwable.getMessage(), throwable)

fun logResultSet(forClass: Any, resultSet: ResultSet)
{
    try {
        val entries: MutableMap<String, String> = hashMapOf()
        val resultMeta = resultSet.metaData

        for (i in 1..resultMeta.columnCount) {
            entries.put(resultMeta.getColumnName(i),
                    resultSet.getString(i) ?: "null")
        }

        logger(forClass).info(
                "DB query on ${resultMeta.getTableName(1)} => $entries")
    }
    catch (e: Throwable) {
        logThrowable(forClass, e)
    }
}
