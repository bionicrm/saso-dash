package io.saso.dash.database.entities.impl;

import io.saso.dash.database.entities.DBAuthToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class DashDBAuthToken implements DBAuthToken
{
    private int id;
    private String access;
    private Optional<String> refresh;
    private Optional<Timestamp> expiresAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fill(ResultSet resultSet) throws SQLException
    {
        id = resultSet.getInt("id");
        access = resultSet.getString("access");
        refresh = Optional.ofNullable(resultSet.getString("refresh"));
        expiresAt = Optional.ofNullable(resultSet.getTimestamp("expires_at"));
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");
    }

    @Override
    public String getTableName()
    {
        return "auth_tokens";
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String getAccess()
    {
        return access;
    }

    @Override
    public Optional<String> getRefresh()
    {
        return refresh;
    }

    @Override
    public Optional<Timestamp> getExpiresAt()
    {
        return expiresAt;
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
