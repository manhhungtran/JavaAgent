package cz.muni.fi.pv168.project;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Tran Manh Hung 433556
 */
public class Assignment
{
    private Long id;
    private AssignmentStatus status;
    private LocalDate startDate;
    private Agent agent;
    private Mission mission;
    
    public Assignment() {}
        
    public Assignment(Long id, AssignmentStatus status, LocalDate startDate, Agent agent, Mission mission) {
        this.id = id;
        this.status = status;
        this.startDate = startDate;
        this.agent = agent;
        this.mission = mission;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    
    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
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
        return "Assignment: { id = " + id + ", status = " + status + ", start date = " + startDate + ", agent = " + agent + ", mission = " + mission + " }";
    }
}
