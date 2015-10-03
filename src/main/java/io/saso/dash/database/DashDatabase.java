package io.saso.dash.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashDatabase implements Database
{
    private static final Logger logger = LogManager.getLogger();



    @Override
    public void initialize()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
