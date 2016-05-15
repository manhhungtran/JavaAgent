package cz.muni.fi.pv168.project;

import java.util.Objects;

/**
 * @author Filip Petrovic (422334)
 */
public class Agent
{
    private Long id;
    private String alias;
    private AgentExperience experience;

    public Agent() {}

    public Agent(Long id, String alias, AgentExperience experience)
    {
        this.id = id;
        this.alias = alias;
        this.experience = experience;
    }
    
    public Long getId() { return id; }
    public String getAlias() { return alias; }
    public AgentExperience getExperience() { return experience; }
    
    public void setId(Long id) { this.id = id; }    
    public void setAlias(String alias) { this.alias = alias; }
    public void setExperience(AgentExperience experience) { this.experience = experience; }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Agent))
        {
            return false;
        }
        
        return id.equals(((Agent)other).id);
    }
    
    @Override public String toString()
    {
        return "Agent: { id = " + id + ", alias = " + alias + ", experience = " + experience + " }";
    }
}
