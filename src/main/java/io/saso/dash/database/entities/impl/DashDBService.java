package io.saso.dash.database.entities.impl;

import io.saso.dash.database.entities.DBService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashDBService implements DBService
{
    private int id;
    private String name;

    @Override
    public void fill(ResultSet resultSet) throws SQLException
    {
        id = resultSet.getInt("id");
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
}
