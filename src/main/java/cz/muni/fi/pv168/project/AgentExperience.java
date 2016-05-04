package cz.muni.fi.pv168.project;

/**
 * @author Filip Petrovic (422334)
 */
public enum AgentExperience
{
    NOVICE("Novice"),
    INTERMEDIATE("Intermediate"),
    EXPERT("Expert"),
    MASTER("Master");
    
    private final String stringValue;
    
    private AgentExperience(String string)
    {
        stringValue = string;
    }
    
    @Override
    public String toString()
    {
        return stringValue;
    }
    
    public static AgentExperience fromString(String string)
    {
        if(string == null)
        {
            throw new IllegalArgumentException("No corresponding AgentExperience enum exists for null string.");
        }
        
        for(AgentExperience experience : AgentExperience.values())
        {
            if(string.equalsIgnoreCase(experience.toString()))
            {
                return experience;
            }
        }
        
        throw new IllegalArgumentException("No corresponding AgentExperience enum found for string: " + string);
    }
}
