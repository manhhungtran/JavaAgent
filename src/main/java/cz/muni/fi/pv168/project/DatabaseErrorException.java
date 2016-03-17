package cz.muni.fi.pv168.project;

/**
 * This exception indicates a problem with database.
 * 
 * @author Filip Petrovic (422334)
 */
public class DatabaseErrorException extends RuntimeException
{
    public DatabaseErrorException(String message)
    {
        super(message);
    }

    public DatabaseErrorException(Throwable cause)
    {
        super(cause);
    }

    public DatabaseErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
