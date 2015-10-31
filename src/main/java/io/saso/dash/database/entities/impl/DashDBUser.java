package io.saso.dash.database.entities.impl;

import io.saso.dash.database.entities.DBUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class DashDBUser implements DBUser
{
    private int id;
    private String name;
    private Optional<String> email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fill(ResultSet resultSet) throws SQLException
    {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
        email = Optional.ofNullable(resultSet.getString("email"));
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");
    }

    @Override
    public String getTableName()
    {
        return "users";
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
    public Optional<String> getEmail()
    {
        return email;
    }

    @Override
    public Timestamp getCreatedAt()
    {
        return createdAt;
    }

    @Override
    public Timestamp getUpdatedAt()
    {
        return updatedAt;
    }
}
