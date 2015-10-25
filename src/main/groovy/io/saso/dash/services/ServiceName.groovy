package io.saso.dash.services

enum ServiceName
{
    GITHUB('github'),
    GOOGLE('google')

    final String name

    ServiceName(String name)
    {
        this.name = name
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    String toString()
    {
        return name
    }
}
