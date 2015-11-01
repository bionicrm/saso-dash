package io.saso.dash.util;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;

public final class Resources
{
    private static final Logger logger = LogManager.getLogger();

    private Resources() { }

    public static String get(String path)
    {
        try {
            if (path.startsWith("/")) {
                return IOUtils.toString(Resources.class.getResource(path));
            }
            else {
                return IOUtils.toString(new FileReader(path));
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
