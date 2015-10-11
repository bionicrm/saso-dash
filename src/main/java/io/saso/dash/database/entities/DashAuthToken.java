package io.saso.dash.database.entities;

import io.saso.dash.util.LoggingUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class DashAuthToken implements AuthToken
{
    private int       id;
    private String    access;
    private String    refresh;
    private Timestamp expiresAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fillFromResultSet(ResultSet resultSet) throws SQLException
    {
        id        = resultSet.getInt("id");
        access    = resultSet.getString("access");
        refresh   = resultSet.getString("refresh");
        expiresAt = resultSet.getTimestamp("expires_at");
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");
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
        return Optional.ofNullable(refresh);
    }

    @Override
    public Optional<Timestamp> getExpiresAt()
    {
        return Optional.ofNullable(expiresAt);
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
        return "auth_tokens";
    }
}
