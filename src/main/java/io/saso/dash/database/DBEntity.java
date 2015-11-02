package io.saso.dash.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBEntity
{
    /**
     * Fills this entity with selected columns from the given result set.
     *
     * @param resultSet the result set to fill this entity's fields with
     *
     * @throws SQLException
     */
    void fill(ResultSet resultSet) throws SQLException;

    /**
     * Gets the ID.
     *
     * @return the ID
     */
    int getId();
}
