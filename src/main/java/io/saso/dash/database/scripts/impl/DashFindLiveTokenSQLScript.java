package io.saso.dash.database.scripts.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.entities.DBAuthToken;
import io.saso.dash.database.entities.DBLiveToken;
import io.saso.dash.database.scripts.EntityReturnableSQLScript;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DashFindLiveTokenSQLScript implements EntityReturnableSQLScript
{
    private final String token;

    @Inject
    public DashFindLiveTokenSQLScript(@Assisted String token)
    {
        this.token = token;
    }

    @Override
    public Class<? extends DBEntity> getEntityClass()
    {
        return DBLiveToken.class;
    }

    @Override
    public void setParametersOn(PreparedStatement statement) throws SQLException
    {
        statement.setString(1, token);
    }

    @Override
    public String getPath()
    {
        return "/sql/find_live_token.sql";
    }
}
