package io.saso.dash.database;

public class UnsupportedTypeException extends RuntimeException
{
    public UnsupportedTypeException()
    {
        super();
    }

    public UnsupportedTypeException(String message)
    {
        super(message);
    }
}
