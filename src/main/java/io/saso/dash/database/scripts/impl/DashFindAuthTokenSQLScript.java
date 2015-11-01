package io.saso.dash.database.scripts.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.entities.DBAuthToken;
import io.saso.dash.database.scripts.EntityReturnableSQLScript;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DashFindAuthTokenSQLScript implements EntityReturnableSQLScript
{
    private final int userId;
    private final String providerName;

    @Inject
    public DashFindAuthTokenSQLScript(@Assisted int userId,
                                      @Assisted String providerName)
    {
        this.userId = userId;
        this.providerName = providerName;
    }

    @Override
    public Class<? extends DBEntity> getEntityClass()
    {
        return DBAuthToken.class;
    }

    @Override
    public void setParametersOn(PreparedStatement statement) throws SQLException
    {
        statement.setInt(1, userId);
        statement.setString(2, providerName);
    }

    @Override
    public String getPath()
    {
        return "/sql/find_auth_token.sql";
    }
}
