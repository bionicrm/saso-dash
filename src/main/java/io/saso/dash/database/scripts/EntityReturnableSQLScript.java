package io.saso.dash.database.scripts;

import io.saso.dash.database.DBEntity;

public interface EntityReturnableSQLScript extends SQLScript
{
    /**
     * Gets the class of the entity that this SQL script ultimately retrieves.
     *
     * @return the class of the entity
     */
    Class<? extends DBEntity> getEntityClass();
}
