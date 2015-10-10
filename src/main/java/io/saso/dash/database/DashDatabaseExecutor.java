package io.saso.dash.database;

import com.google.inject.Inject;

import java.sql.*;
import java.util.Optional;

public class DashDatabaseExecutor implements DatabaseExecutor
{
    private final Database db;

    private Optional<Connection> lastConn = Optional.empty();

    @Inject
    public DashDatabaseExecutor(Database db)
    {
        this.db = db;
    }

    @Override
    public ResultSet execute(String sql, Object... args) throws SQLException
    {
        lastConn = Optional.of(db.getConnection());

        final PreparedStatement statement =
                lastConn.get().prepareStatement(sql);

        // set statement parameters based on type
        for (int i = 0; i < args.length; i++) {
            final Object o = args[i];
            final int paramI = i + 1;

            if (o instanceof Boolean) {
                statement.setBoolean(paramI, (boolean) o);
            }
            else if (o instanceof Integer) {
                statement.setInt(paramI, (int) o);
            }
            else if (o instanceof Double) {
                statement.setDouble(paramI, (double) o);
            }
            else if (o instanceof String) {
                statement.setString(paramI, (String) o);
            }
            else if (o instanceof Timestamp) {
                statement.setTimestamp(paramI, (Timestamp) o);
            }
            else {
                throw new UnsupportedTypeException(
                        "Unsupported type for args[" + i + "]");
            }
        }

        return statement.executeQuery();
    }

    @Override
    public void close() throws SQLException
    {
        if (lastConn.isPresent()) {
            db.closeConnection();
        }
    }
}
