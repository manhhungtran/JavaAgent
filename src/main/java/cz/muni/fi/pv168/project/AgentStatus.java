package cz.muni.fi.pv168.project;

/**
 * @author Filip Petrovic (422334)
 */
public enum AgentStatus
{
    AVAILABLE ("Available"),
    ON_MISSION ("On Mission"),
    DECEASED ("Deceased"),
    RETIRED ("Retired");
    
    private final String name;       

    private AgentStatus(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
       return this.name;
    }
}
