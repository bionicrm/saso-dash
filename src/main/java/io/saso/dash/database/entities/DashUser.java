package io.saso.dash.database.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class DashUser implements User
{
    private int       id;
    private String    name;
    private String    email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fillFromResultSet(ResultSet resultSet) throws SQLException
    {
        id        = resultSet.getInt("id");
        name      = resultSet.getString("name");
        email     = resultSet.getString("email");
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");
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
        return Optional.ofNullable(email);
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

    @Override
    public String getTableName()
    {
        return "users";
    }
}
