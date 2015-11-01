package io.saso.dash.util;

import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;

public final class Resources
{
    private Resources() { }

    public static String get(String path)
    {
        try {
            if (path.startsWith("/")) {
                return IOUtils.toString(Resources.class.getResource(path));
            }

            return IOUtils.toString(new FileReader(path));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
