package io.saso.dash.database.impl;

import com.google.inject.Singleton;
import io.saso.dash.database.DBScriptRepository;
import io.saso.dash.util.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
public class DashDBScriptRepository implements DBScriptRepository
{
    private static final Logger logger = LogManager.getLogger();

    /**
     * The number of scripts found in src/main/resources/sql. Not required to
     * be correct; it is simply used as the initial map size for the cache.
     */
    private static final int SCRIPT_COUNT = 5;

    private final Map<String, String> cache =
            new ConcurrentHashMap<>(SCRIPT_COUNT);

    @Override
    public String getSQL(String scriptName)
    {
        return cache.computeIfAbsent(scriptName, key -> readSQL(scriptName));
    }

    /**
     * Reads an SQL file's contents. E.g. if {@code scriptName} is
     * {@code "my_script"}, then resources/sql/my_script.sql will be read.
     *
     * @param scriptName the name of the SQL script
     *
     * @return the script's contents
     */
    private String readSQL(String scriptName)
    {
        long start = System.nanoTime();

        try {
            return Resources.get("/sql/" + scriptName + ".sql");
        }
        finally {
            long end = System.nanoTime();
            logger.debug("Read {}.sql in about {}µs", scriptName,
                    TimeUnit.NANOSECONDS.toMicros(end - start));
        }
    }
}
