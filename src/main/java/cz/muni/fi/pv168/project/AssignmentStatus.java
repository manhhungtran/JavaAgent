package cz.muni.fi.pv168.project;

/**
 * @author Filip Petrovic (422334)
 */
public enum AssignmentStatus 
{
    IN_PROGRESS("In Progress"),
    SUCCEEDED("Succeeded"),
    FAILED("Failed");
    
    private final String stringValue;
    
    private AssignmentStatus(String string)
    {
        stringValue = string;
    }
    
    @Override
    public String toString()
    {
        return stringValue;
    }
    
    public static AssignmentStatus fromString(String string)
    {
        if(string == null)
        {
            throw new IllegalArgumentException("No corresponding AssignmentStatus enum exists for null string.");
        }
        
        for(AssignmentStatus status : AssignmentStatus.values())
        {
            if(string.equalsIgnoreCase(status.toString()))
            {
                return status;
            }
        }
        
        throw new IllegalArgumentException("No corresponding AssignmentStatus enum found for string: " + string);
    }
}
