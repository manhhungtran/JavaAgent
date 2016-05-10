
package cz.muni.fi.pv168.project;

/**
 * @author Tran Manh Hung (433556)
 */
public enum MissionDifficulty 
{
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    IMPOSSIBLE("Impossible"),
    CHUCKNORRIS("Chuck Norris");
    
    private final String stringValue;
    
    private MissionDifficulty(String string)
    {
        stringValue = string;
    }
    
    @Override
    public String toString()
    {
        return stringValue;
    }
    
    public static MissionDifficulty fromString(String string)
    {
        if(string == null)
        {
            throw new IllegalArgumentException("No corresponding MissionDifficulty enum exists for null string.");
        }
        
        for(MissionDifficulty difficulty : MissionDifficulty.values())
        {
            if(string.equalsIgnoreCase(difficulty.toString()))
            {
                return difficulty;
            }
        }
        
        throw new IllegalArgumentException("No corresponding MissionDifficulty enum found for string: " + string);
    }
}
