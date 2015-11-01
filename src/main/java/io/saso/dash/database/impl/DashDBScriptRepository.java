package io.saso.dash.database.impl;

import com.google.inject.Singleton;
import io.saso.dash.database.DBScriptRepository;
import io.saso.dash.util.Resources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DashDBScriptRepository implements DBScriptRepository
{
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
        return Resources.get("/sql/" + scriptName + ".sql");
    }
}
