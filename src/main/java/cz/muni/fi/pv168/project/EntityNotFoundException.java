package cz.muni.fi.pv168.project;

/**
 * 
 * This exception is thrown when update or similar operation is performed
 * on entity which doesn't exist in database.
 * 
 * @author Filip Petrovic
 */
public class EntityNotFoundException extends RuntimeException
{
    public EntityNotFoundException(String message)
    {
        super(message);
    }
    
    public EntityNotFoundException(Throwable cause)
    {
        super(cause);
    }

    public EntityNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
