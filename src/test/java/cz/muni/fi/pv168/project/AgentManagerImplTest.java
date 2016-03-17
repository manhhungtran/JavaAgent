package cz.muni.fi.pv168.project;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentManagerImplTest
{
    private AgentManager manager;
    private DataSource dataSource;
    
    @Before
    public void setUp() throws SQLException
    {
        dataSource = prepareDataSource();
        try(Connection connection = dataSource.getConnection())
        {
            connection.prepareStatement("CREATE TABLE Agent ("
                    + "id bigint primary key generated always as identity,"
                    + "alias varchar(50),"
                    + "status varchar(30),"
                    + "experience varchar(30))").executeUpdate();
        }
        manager = new AgentManagerImpl(dataSource);
    }
    
    private static DataSource prepareDataSource() throws SQLException
    {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:agentmanagerimpl-test");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
    
    @After
    public void tearDown() throws SQLException
    {
        try(Connection connection = dataSource.getConnection())
        {
            connection.prepareStatement("DROP TABLE Agent").executeUpdate();
        }
    }
    
    @Test
    public void addNewAgent()
    {
        Agent agent = createAgent(null, "Agent", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        manager.addAgent(agent);
        
        assertNotNull("Agent's id is null.", agent.getId());
        
        Agent retrieved = manager.getAgent(agent.getId());
        assertEquals("Saved and retrieved agents' ids are different.", agent.getId(), retrieved.getId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNullAgent() throws Exception
    {
        manager.addAgent(null);
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void updateNonExistentAgent() throws Exception
    {
        Agent nonExistentAgent = createAgent(1L, "Non-existent agent", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        manager.updateAgent(nonExistentAgent);
    }
    
    @Test
    public void getAgentsWithStatus()
    {
        Agent first = createAgent(null, "First", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        Agent second = createAgent(null, "Second", AgentStatus.ON_MISSION, AgentExperience.NOVICE);
        Agent third = createAgent(null, "Third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent fourth = createAgent(null, "Fourth", AgentStatus.RETIRED, AgentExperience.MASTER);
        
        manager.addAgent(first);
        manager.addAgent(second);
        manager.addAgent(third);
        manager.addAgent(fourth);
        
        List<Agent> availableAgents = manager.getAgentsWithStatus(AgentStatus.AVAILABLE);
        for(Agent agent : availableAgents)
        {
            if(!agent.getStatus().equals(AgentStatus.AVAILABLE))
            {
                fail("Expected agent status: AVAILABLE, returned agent status: " + agent.getStatus().name());
            }
        }
        
        List<Agent> others = manager.getAllAgents();
        others.removeAll(availableAgents);
        for(Agent agent : others)
        {
            if(agent.getStatus().equals(AgentStatus.AVAILABLE))
            {
                fail("Following agent with desired status AVAILABLE wasn't returned:" + agent);
            }
        }
    }
    
    private static Agent createAgent(Long id, String alias, AgentStatus status, AgentExperience experience)
    {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setAlias(alias);
        agent.setStatus(status);
        agent.setExperience(experience);
        
        return agent;
    }
}
