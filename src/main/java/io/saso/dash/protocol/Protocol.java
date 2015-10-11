package io.saso.dash.protocol;

/**
 * Represents types of messages that can be interpreted by or read from the
 * client.
 */
public enum Protocol
{
    TOO_MANY_CONCURRENT_CONNECTIONS(0),
    ;

    private final int id;

    Protocol(int id)
    {
        this.id = id;
    }

    /**
     * Gets the ID.
     *
     * @return the ID
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the ID in String form.
     *
     * @return the ID in String form
     */
    @Override
    public String toString()
    {
        return String.valueOf(id);
    }
}
