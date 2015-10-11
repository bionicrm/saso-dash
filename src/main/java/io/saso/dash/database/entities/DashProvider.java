package io.saso.dash.database.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashProvider implements Provider
{
    private int    id;
    private String name;

    @Override
    public void fillFromResultSet(ResultSet resultSet) throws SQLException
    {
        id   = resultSet.getInt("id");
        name = resultSet.getString("name");
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getTableName()
    {
        return "providers";
    }
}
