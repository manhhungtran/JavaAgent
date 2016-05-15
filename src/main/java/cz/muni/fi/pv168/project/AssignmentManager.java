package cz.muni.fi.pv168.project;

import java.util.List;

/**
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public interface AssignmentManager
{
    /**
     * Adds new assignment.
     * @param assignment Assignment to be added.
     * @throws IllegalArgumentException when assignment is null or assignment id is already set.
     * @throws DatabaseErrorException when database error occurs.
     */
    void addAssignment(Assignment assignment);
    
    /**
     * Updates existing assignment.
     * @param assignment Assignment to be updated.
     * @throws IllegalArgumentException when assignment is null or assignment id is null.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when assignment with given id doesn't exist.
     */
    void updateAssignment(Assignment assignment);
    
    /**
     * Deletes existing assignment.
     * @param id Id of an assignment to be deleted.
     * @throws IllegalArgumentException when assignment is null or assignment id is null.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when assignment with given id doesn't exist.
     */
    void deleteAssignment(Long id);
    
    /**
     * Returns existing assignment with given id.
     * @param id Id of an assignment to be returned.
     * @return Existing assignment with given id.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when assignment with given id doesn't exist.
     */
    Assignment getAssignment(Long id);
    
    /**
     * Returns list of all assignments.
     * @return List of all assignments.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Assignment> getAllAssignments();
    
    /**
     * Returns list of all assignments with given status.
     * @param status Desired status.
     * @return List of all assignments with given status.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Assignment> getAssignmentsWithStatus(AssignmentStatus status);
    
    /**
     * Returns list of all assignments for given agent.
     * @param agent Desired agent.
     * @return List of all assignments for given agent.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Assignment> getAssignmentsForAgent(Agent agent);
    
    /**
     * Returns list of all assignments for given mission.
     * @param mission Desired mission.
     * @return List of all assignments for given mission.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Assignment> getAssignmentsForMission(Mission mission);
}
