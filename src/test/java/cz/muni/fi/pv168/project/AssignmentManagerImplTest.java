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
    
        agentOne = createAgent(null, "First", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        agentTwo = createAgent(null, "Second", AgentStatus.ON_MISSION, AgentExperience.MASTER);
        agentThree = createAgent(null, "Third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        
        missionOne = createMission(null, "Testing Mission.", LocalDate.now(), 500, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
        missionTwo = createMission(null, "Write all unit tests, now!!!", LocalDate.now(), 500, MissionDifficulty.IMPOSSIBLE, MissionStatus.NEW);
        
        first = createAssignment(null, agentOne, missionOne);
        second = createAssignment(null, agentTwo, missionOne);
        third = createAssignment(null, agentThree, missionTwo);
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
        
        Assignment update = createAssignment(first.getId(), agentTwo, missionOne);
        assignmentManager.updateAssignment(update);
        
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

        assignmentManager.deleteAssignment(first);

        assertThat(assignmentManager.getAssignment(second.getId())).isNotNull();
        
        expectedException.expect(EntityNotFoundException.class);
        assignmentManager.getAssignment(first.getId());
    }
    
    @Test
    public void testGetAllAssignments() 
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        agentManager.addAgent(agentThree);
        missionManager.addMission(missionOne);
        assignmentManager.addAssignment(first);
        assignmentManager.addAssignment(second);
        List<Assignment> result = assignmentManager.getAllAssignments();
        
        assertTrue(result.size() == 2);
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
    }
    
    @Test
    public void testGetAssignmentsForAgent() 
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
        
        assertTrue(result.size() == 1);
        assertTrue(result.contains(first));
        assertTrue(!result.contains(second));
    }
    
    @Test
    public void testGetAssignmentsForMission() 
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
