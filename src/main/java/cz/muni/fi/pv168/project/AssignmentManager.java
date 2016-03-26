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
     * @param assignment Assignment to be deleted.
     * @throws IllegalArgumentException when assignment is null or assignment id is null.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when assignment with given id doesn't exist.
     */
    void deleteAssignment(Assignment assignment);
    
    List<Assignment> getAllAssignments();
    List<Assignment> getAssignmentsForMission(Mission mission);
    List<Assignment> getAssignmentsForAgent(Agent agent);
}
