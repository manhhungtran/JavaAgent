package cz.muni.fi.pv168.project;

import java.util.Objects;

/**
 * @author Tran Manh Hung 433556
 */
public class Assignment {
    private Mission mission;
    private Agent agent;
    private Long id;

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Assignment)) {
            return false;
        }
        
        return id.equals(((Assignment)other).id);
    }

    @Override
    public String toString() {
        return "Assigment{" + "mission=" + mission + ", agent=" + agent + ", id=" + id + '}';
    }
}
