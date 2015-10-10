package io.saso.dash.database;

import java.sql.Connection;

public interface Database
{
    Connection getConnection();

    void shutdown();
}
