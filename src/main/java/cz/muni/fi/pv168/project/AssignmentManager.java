package cz.muni.fi.pv168.project;

import java.util.List;

/**
 *
 * @author Tran Manh Hung 433556
 */
public interface AssignmentManager {
    void addAssigment(Assignment assignment);
    void updateAssigment(Assignment assignment);
    List<Assignment> getAssigmentsForMission(Mission mission);
    List<Assignment> getAssigmentsForAgent(Agent agent);
    List<Assignment> getAllAssigments();
    void deleteAssignment(Assignment assignment);
}
