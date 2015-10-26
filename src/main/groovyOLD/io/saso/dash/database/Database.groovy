package io.saso.dash.database

import java.sql.Connection

interface Database
{
    Connection getConnection()
}
