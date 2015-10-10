package io.saso.dash.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a database entity.
 */
public interface DBEntity
{
    /**
     * Fills this entity's fields with columns from {@code resultSet}.
     *
     * @param resultSet the ResultSet to fill this entity's fields with
     *
     * @throws SQLException
     */
    void fillFromResultSet(ResultSet resultSet) throws SQLException;
}
