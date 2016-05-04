package cz.muni.fi.pv168.project;

/**
 * @author Filip Petrovic (422334)
 */
public enum AgentStatus
{
    AVAILABLE("Available"),
    ON_MISSION("On Mission"),
    DECEASED("Deceased"),
    RETIRED("Retired");
    
    private final String stringValue;
    
    private AgentStatus(String string)
    {
        stringValue = string;
    }
    
    @Override
    public String toString()
    {
        return stringValue;
    }
    
    public static AgentStatus fromString(String string)
    {
        if(string == null)
        {
            throw new IllegalArgumentException("No corresponding AgentStatus enum exists for null string.");
        }
        
        for(AgentStatus status : AgentStatus.values())
        {
            if(string.equalsIgnoreCase(status.toString()))
            {
                return status;
            }
        }
        
        throw new IllegalArgumentException("No corresponding AgentStatus enum found for string: " + string);
    }
}
