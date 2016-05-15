package cz.muni.fi.pv168.project;

import java.util.Objects;

/**
 * @author Tran Manh Hung (433556)
 */
public class Mission 
{
    private Long id;
    private String codename;
    private String description;
    private MissionDifficulty difficulty;

    public Mission() {}

    public Mission(Long id, String codename, String description, MissionDifficulty difficulty) {
        this.id = id;
        this.codename = codename;
        this.description = description;
        this.difficulty = difficulty;
    }
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }  

    public String getDescription()
    {
        return description;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public MissionDifficulty getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(MissionDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    @Override
    public int hashCode()
    {
        if(id == null)
            return 0;
        
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Mission))
        {
            return false;
        }
        
        return id.equals(((Mission)other).id);
    }

    @Override
    public String toString() 
    {
        return "Mission: { id = " + id + ", description = " + description + ", difficulty = " + difficulty + " }";
    }
}
