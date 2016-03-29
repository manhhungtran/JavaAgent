package cz.muni.fi.pv168.project;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
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
        missionOne = createMission(null, "Write all unit tests, now!!!", LocalDate.now(), 500, MissionDifficulty.IMPOSSIBLE, MissionStatus.NEW);
        
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
        
        List<Assignment> result = assignmentManager.getAssignmentsForAgent(first.getAgent());
        assertEquals("Retrieved assignment differs from the saved one.", first.getId(), result.get(0).getId());
    }
    
    @Test
    public void updateAssignment()
    {
        agentManager.addAgent(agentOne);
        agentManager.addAgent(agentTwo);
        missionManager.addMission(missionOne);
        
        assignmentManager.addAssignment(first);
        //Assignment update = createAssignment();
    }
    
    @Test
    public void deleteAssignment()
    {
        
    }
}
