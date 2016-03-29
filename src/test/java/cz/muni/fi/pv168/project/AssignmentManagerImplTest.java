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
    private AssignmentManager manager;
    private Assignment first;
    private Assignment second;
    private Assignment third;

    @Before
    public void setUp() throws SQLException
    {
        dataSource = prepareDataSource("memory:agentmanagerimpl-test");
        executeSqlScript(dataSource, AssignmentManager.class.getResource("createTables.sql"));
        manager = new AssignmentManagerImpl(dataSource);
    
        Mission mission = createMission(1L, "Testing Mission.", LocalDate.now(), 500, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
        Agent agentfirst = createAgent(1L, "First", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        Agent agentsecond = createAgent(2L, "Second", AgentStatus.ON_MISSION, AgentExperience.NOVICE);
        Agent agentthird = createAgent(3L, "Third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        
        first = createAssignment(null, agentfirst, mission);
        second = createAssignment(null, agentsecond, mission);
        third = createAssignment(null, agentthird, mission);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNullAssignment() throws Exception
    {
        manager.addAssignment(null);
    }
    
    @Test
    public void testAddAssignment()
    {
        manager.addAssignment(first);
        assertNotNull("Saved assignment has null ID", first.getId());
        List<Assignment> result = manager.getAssignmentsForAgent(first.getAgent());
        assertEquals("Retrieved assignment differs from the saved one", first.getId(), result.get(0).getId());
    }
    
    @Test
    public void testUpdateAssignment()
    {
        
    }
    
}
