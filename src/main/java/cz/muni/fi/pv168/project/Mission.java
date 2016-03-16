package cz.muni.fi.pv168.project;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Tran Manh Hung 433556
 */
public class Mission {
    private Long id;
    private String description;
    private LocalDate start;
    private int duration;
    private MissionDifficulty difficulty;
    private MissionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id)
    {
        if (this.id != null)
        {
            throw new UnsupportedOperationException("Mission ID cannot be changed once set.");
        }
        this.id = id;
    }  

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public MissionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(MissionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mission other = (Mission) obj;
        return Objects.equals(this.id, other.id);
    }
}
