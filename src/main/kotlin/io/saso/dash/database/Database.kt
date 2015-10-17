package io.saso.dash.database

import java.sql.Connection

public interface Database
{
    val connection: Connection get
}
