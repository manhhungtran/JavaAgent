package cz.muni.fi.pv168.project;

/**
 * @author Tran Manh Hung (433556)
 */
public enum MissionStatus 
{
    NEW("New"),
    ONGOING("Ongoing"),
    SUCCEDED("Succeeded"),
    FAILED("Failed");
    
    private final String stringValue;
    
    private MissionStatus(String string)
    {
        stringValue = string;
    }
    
    @Override
    public String toString()
    {
        return stringValue;
    }
    
    public static MissionStatus fromString(String string)
    {
        if(string == null)
        {
            throw new IllegalArgumentException("No corresponding MissionStatus enum exists for null string.");
        }
        
        for(MissionStatus status : MissionStatus.values())
        {
            if(string.equalsIgnoreCase(status.toString()))
            {
                return status;
            }
        }
        
        throw new IllegalArgumentException("No corresponding MissionStatus enum found for string: " + string);
    }
}
