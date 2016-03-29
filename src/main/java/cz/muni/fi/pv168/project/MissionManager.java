package cz.muni.fi.pv168.project;

import java.util.List;

/**
 *
 * @author Tran Manh Hung (433556)
 */
public interface MissionManager 
{
    /**
     * Add mission into collection
     * @param mission Mission that will be added
     * @throws IllegalArgumentException mission is null or mission id is already set
     * @throws DatabaseErrorException database error occurs
     */
    void addMission(Mission mission);
    
    /**
     * Modify mission
     * @param mission Mission that will be modified
     * @throws IllegalArgumentException when mission or his id is null
     * @throws DatabaseErrorException when database error occurs
     * @throws EntityNotFoundException when mission with given id doesn't exist
     */
    void updateMission(Mission mission);
    
    /**
     * Get mission based on ID
     * @param a ID
     * @return mission     
     * @throws DatabaseErrorException when database error occurs
     * @throws EntityNotFoundException when mission with given id doesn't exist
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
     * @throws DatabaseErrorException when database error occurs
     */
    List<Mission> getMissionsWithStatus(MissionStatus status);
    
    /**
     * Get collection of missions based on difficulty
     * @param difficulty Desire difficulty
     * @return Collection of missions
     * @throws DatabaseErrorException when database error occurs
     */
    List<Mission> getMissionsWithDifficulty(MissionDifficulty difficulty);
    
    /**
     * Deletes mission
     * @param mission Mission to be deleted
     * @throws IllegalArgumentException when mission or his id is null
     * @throws DatabaseErrorException when database error occurs
     * @throws EntityNotFoundException when mission with given id doesn't exist
     */
    void deleteMission(Mission mission);
}
