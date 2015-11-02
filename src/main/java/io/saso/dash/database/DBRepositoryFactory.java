package io.saso.dash.database;

import io.saso.dash.database.entities.DBAuthToken;

public interface DBRepositoryFactory
{
    DBRepository create(DBAuthToken authToken);
}
