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
    
    /**
     * Returns existing assignment with given id.
     * @param id Id of an assignment to be returned.
     * @return Existing assignment with given id.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when assignment with given id doesn't exist.
     */
    Assignment getAssignment(Long id);
    
    /**
     * Get all assignments
     * @return Collection of all assignments in database
     */
    List<Assignment> getAllAssignments();

    /**
     * Get collection of assignments based on mission
     * @param mission
     * @return Collection of assignments
     * @throws DatabaseErrorException when database error occurs
     */
    List<Assignment> getAssignmentsForMission(Mission mission);
    
    /**
     * Get collection of assignments based on agent
     * @param agent
     * @return Collection of assignments
     * @throws DatabaseErrorException when database error occurs
     */
    List<Assignment> getAssignmentsForAgent(Agent agent);
}
