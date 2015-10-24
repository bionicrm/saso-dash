package io.saso.dash.services

enum Service
{
    GITHUB('github'),
    GOOGLE('google')

    final String name

    Service(String name)
    {
        this.name = name
    }

    /**
     * Gets the name of this service.
     *
     * @return the name
     */
    @Override
    String toString()
    {
        name
    }
}
