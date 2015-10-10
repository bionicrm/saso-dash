package io.saso.dash.database;

import com.google.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DashDatabaseExecutor implements DatabaseExecutor
{
    private final Database db;

    @Inject
    public DashDatabaseExecutor(Database db)
    {
        this.db = db;
    }

    @Override
    public ResultSet execute(String sql, Object... args) throws SQLException
    {
        final PreparedStatement statement =
                db.getConnection().prepareStatement(sql);

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
}
