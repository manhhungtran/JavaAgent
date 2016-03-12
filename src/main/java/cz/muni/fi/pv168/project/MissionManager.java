package cz.muni.fi.pv168.project;

import java.util.List;

/**
 *
 * @author Tran Manh Hung 433556
 */
public interface MissionManager {
    
    /**
     * Add mission into collection
     * @param mission Mission that will be added
     */
    void addMission(Mission mission);
    
    /**
     * Modify mission
     * @param mission Mission that will be modified
     */
    void updateMission(Mission mission);
    
    /**
     * Get mission based on ID
     * @param a ID
     * @return mission
     */
    Mission getMission(Long a);
    
    /**
     * Get all missions
     * @return Collection of all missions in database
     */
    List<Mission> getAllMissions();
    
    /**
     * Get collection of missions based on difficulty
     * @param status
     * @return Collection of missions
     */
    List<Mission> getMissionsWithStatus(MissionStatus status);
    
    /**
     * Get collection of missions based on difficulty
     * @param difficulty Desire difficulty
     * @return Collection of missions
     */
    List<Mission> getMissionsWithDifficulty(MissionDifficulty difficulty);
    
}
