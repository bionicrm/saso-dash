package io.saso.dash.database.entities

import io.saso.dash.database.DBEntity

interface DBProvider extends DBEntity
{
    String getName()
}
