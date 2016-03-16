package cz.muni.fi.pv168.project;

import java.util.Objects;

/**
 * @author Filip Petrovic
 */
public class Agent
{
    private Long id;
    private String alias;
    private AgentStatus status;
    private AgentExperience experience;
    
    public Long getId() { return id; }
    public String getAlias() { return alias; }
    public AgentStatus getStatus() { return status; }
    public AgentExperience getExperience() { return experience; }
    
    public void setId(Long id) { this.id = id; }    
    public void setAlias(String alias) { this.alias = alias; }
    public void setStatus(AgentStatus status) { this.status = status; }
    public void setExperience(AgentExperience experience) { this.experience = experience; }
    
    @Override public String toString()
    {
        return "{ " + alias + ", " + id + ", " + status + ", " + experience + " }";
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

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
