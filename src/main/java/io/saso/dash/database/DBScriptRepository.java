package io.saso.dash.database;

public interface DBScriptRepository
{
    /**
     * Gets the SQL for the given script.
     *
     * @param scriptName the name of the script
     *
     * @return the SQL contents of the script
     */
    String getSQL(String scriptName);
}
