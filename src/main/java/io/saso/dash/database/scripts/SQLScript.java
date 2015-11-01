package io.saso.dash.database.scripts;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLScript
{
    /**
     * Gets the path to this SQL script, relative to the resource/sql directory.
     *
     * @return the path to this SQL script
     */
    String getPath();

    /**
     * Sets this script's parameters on the given prepared statement.
     *
     * @param statement the statement to set parameters on
     */
    void setParametersOn(PreparedStatement statement) throws SQLException;
}
