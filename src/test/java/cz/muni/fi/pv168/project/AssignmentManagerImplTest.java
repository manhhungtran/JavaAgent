package cz.muni.fi.pv168.project;

import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public class AssignmentManagerImplTest extends SetupBaseTest
{
    private AssignmentManager assignmentManager;
    private AgentManager agentManager;
    private MissionManager missionManager;
    
    private Agent agentOne;
    private Agent agentTwo;
    private Agent agentThree;
    
    private Mission missionOne;
    private Mission missionTwo;
    
    private Assignment first;
    private Assignment second;
    private Assignment third;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws SQLException
    {
        dataSource = prepareDataSource("memory:agentmanagerimpl-test");
        executeSqlScript(dataSource, AssignmentManager.class.getResource("createTables.sql"));
        
        agentManager = new AgentManagerImpl(dataSource);
        missionManager = new MissionManagerImpl(dataSource);
        assignmentManager = new AssignmentManagerImpl(dataSource);
    
        agentOne = createAgent(null, "First", AgentExperience.NOVICE);
        agentTwo = createAgent(null, "Second", AgentExperience.MASTER);
        agentThree = createAgent(null, "Third", AgentExperience.EXPERT);
        
        missionOne = createMission(null, "Testing Mission.", "Do tests.", MissionDifficulty.CHUCKNORRIS);
        missionTwo = createMission(null, "Another one", "Write all unit tests, now!!!", MissionDifficulty.IMPOSSIBLE);
        
        first = createAssignment(null, AssignmentStatus.IN_PROGRESS, LocalDate.now(), agentOne, missionOne);
        second = createAssignment(null, AssignmentStatus.FAILED, LocalDate.now(), agentTwo, missionOne);
        third = createAssignment(null, AssignmentStatus.SUCCEEDED, LocalDate.now(), agentThree, missionTwo);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNullAssignment() throws Exception
    {
        assignmentManager.addAssignment(null);
    }
    
    @Test
    public void addAssignment()
    {
        agentManager.addAgent(agentOne);
        missionManager.addMission(missionOne);
        assignmentManager.addAssignment(first);
        
        assertNotNull("Saved assignment has null ID.", first.getId());
        
        Assignment result = assignmentManager.getAssignment(first.getId());
        assertEquals("Retrieved assignment differs from the saved one.", first.getId(), result.getId());
    }
    
    @Test
    public void updateAssignment()
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        missionManager.addMission(missionOne);
        assignmentManager.addAssignment(first);
        
        Assignment update = createAssignment(first.getId(), AssignmentStatus.SUCCEEDED, LocalDate.now(), agentTwo, missionOne);
        assignmentManager.updateAssignment(update);
        
        assertEquals("Updated assignment's id is incorrect.", update.getId(), assignmentManager.getAssignment(update.getId()).getId());
        assertEquals("Updated assignment's status is incorrect.", update.getStatus(), assignmentManager.getAssignment(update.getId()).getStatus());
        assertEquals("Updated assignment's start date is incorrect.", update.getStartDate(), assignmentManager.getAssignment(update.getId()).getStartDate());
        assertEquals("Updated assignment's agent is incorrect.", update.getAgent(), assignmentManager.getAssignment(update.getId()).getAgent());
        assertEquals("Updated assignment's mission is incorrect.", update.getMission(), assignmentManager.getAssignment(update.getId()).getMission());
    }
    
    @Test
    public void deleteAssignment()
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        missionManager.addMission(missionOne);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);

        assertThat(assignmentManager.getAssignment(first.getId())).isNotNull();
        assertThat(assignmentManager.getAssignment(second.getId())).isNotNull();

        assignmentManager.deleteAssignment(first.getId());

        assertThat(assignmentManager.getAssignment(second.getId())).isNotNull();
        
        expectedException.expect(DatabaseErrorException.class);
        assignmentManager.getAssignment(first.getId());
    }
    
    @Test
    public void getAllAssignments() 
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        agentManager.addAgent(agentThree);
        missionManager.addMission(missionOne);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);
        List<Assignment> result = assignmentManager.getAllAssignments();
        
        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
    }
    
    @Test
    public void getAssignmentsWithStatus()
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        agentManager.addAgent(agentThree);
        missionManager.addMission(missionOne);
        missionManager.addMission(missionTwo);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);
        assignmentManager.addAssignment(third);
        
        List<Assignment> assignmentsInProgress = assignmentManager.getAssignmentsWithStatus(AssignmentStatus.IN_PROGRESS);
        for(Assignment assignment : assignmentsInProgress)
        {
            if(!assignment.getStatus().equals(AssignmentStatus.IN_PROGRESS))
            {
                fail("Following assignment with different status than IN_PROGRESS was returned: " + assignment);
            }
        }
        
        List<Assignment> others = assignmentManager.getAllAssignments();
        others.removeAll(assignmentsInProgress);
        for(Assignment assignment : others)
        {
            if(assignment.getStatus().equals(AssignmentStatus.IN_PROGRESS))
            {
                fail("Following assignment with desired status IN_PROGRESS wasn't returned: " + assignment);
            }
        }
    }
    
    @Test
    public void getAssignmentsForAgent() 
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        agentManager.addAgent(agentThree);
        missionManager.addMission(missionOne);
        missionManager.addMission(missionTwo);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);
        assignmentManager.addAssignment(third);
        List<Assignment> result = assignmentManager.getAssignmentsForAgent(agentOne);

        assertEquals(1, result.size());
        assertTrue(result.contains(first));
        assertTrue(!result.contains(second));
        assertTrue(!result.contains(third));
    }
    
    @Test
    public void getAssignmentsForMission() 
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        agentManager.addAgent(agentThree);
        missionManager.addMission(missionOne);
        missionManager.addMission(missionTwo);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);
        assignmentManager.addAssignment(third);
        List<Assignment> result = assignmentManager.getAssignmentsForMission(missionOne);
        
        assertTrue(result.size() == 2);
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
    }
}
