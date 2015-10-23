package io.saso.dash.util

import org.apache.commons.io.IOUtils

final class Resources
{
    private Resources() { /* empty */ }

    static String get(String path)
    {
        def final stream = {
            if (path.startsWith('/')) {
                Resources.getResourceAsStream(path)
            }
            else {
                new FileInputStream(path)
            }
        }

        IOUtils.toString(stream)
    }
}
