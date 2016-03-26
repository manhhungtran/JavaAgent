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
import java.util.List;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
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
        executeSqlScript(dataSource, AgentManager.class.getResource("createTables.sql"));
        manager = new AgentManagerImpl(dataSource);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        executeSqlScript(dataSource, AgentManager.class.getResource("dropTables.sql"));
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
    public void getAllAgents()
    {
        assertThat(manager.getAllAgents().isEmpty());
        
        Agent first = createAgent(null, "First", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent second = createAgent(null, "Second", AgentStatus.ON_MISSION, AgentExperience.NOVICE);
        Agent third = createAgent(null, "Third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent fourth = createAgent(null, "Fourth", AgentStatus.RETIRED, AgentExperience.MASTER);
        
        manager.addAgent(first);
        manager.addAgent(second);
        manager.addAgent(third);
        manager.addAgent(fourth);
        
        assertThat(manager.getAllAgents()).usingFieldByFieldElementComparator().containsOnly(first, second, third, fourth);
    }
    
    @Test
    public void getAgentsWithExperience()
    {
        Agent first = createAgent(null, "First", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent second = createAgent(null, "Second", AgentStatus.ON_MISSION, AgentExperience.NOVICE);
        Agent third = createAgent(null, "Third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent fourth = createAgent(null, "Fourth", AgentStatus.RETIRED, AgentExperience.MASTER);
        
        manager.addAgent(first);
        manager.addAgent(second);
        manager.addAgent(third);
        manager.addAgent(fourth);
        
        List<Agent> experts = manager.getAgentsWithExperience(AgentExperience.EXPERT);
        for(Agent agent : experts)
        {
            if(!agent.getExperience().equals(AgentExperience.EXPERT))
            {
                fail("Following agent with different experience than EXPERT was returned: " + agent);
            }
        }
        
        List<Agent> others = manager.getAllAgents();
        others.removeAll(experts);
        for(Agent agent : others)
        {
            if(agent.getExperience().equals(AgentExperience.EXPERT))
            {
                fail("Following agent with desired experience EXPERT wasn't returned: " + agent);
            }
        }
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
                fail("Following agent with different status than AVAILABLE was returned: " + agent);
            }
        }
        
        List<Agent> others = manager.getAllAgents();
        others.removeAll(availableAgents);
        for(Agent agent : others)
        {
            if(agent.getStatus().equals(AgentStatus.AVAILABLE))
            {
                fail("Following agent with desired status AVAILABLE wasn't returned: " + agent);
            }
        }
    }
    
    private static DataSource prepareDataSource() throws SQLException
    {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:agentmanagerimpl-test");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
    
    private static String[] readSqlStatements(URL url)
    {
        try(InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8"))
        {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            
            while(true)
            {
                int count = reader.read(buffer);
                if(count < 0)
                {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Cannot read SQL statement: " + url, ex);
        }
    }
    
    private static void executeSqlScript(DataSource dataSource, URL scriptUrl) throws SQLException
    {
        try(Connection connection = dataSource.getConnection())
        {
            for(String sqlStatement : readSqlStatements(scriptUrl))
            {
                if(!sqlStatement.trim().isEmpty()) 
                {
                    connection.prepareStatement(sqlStatement).executeUpdate();
                }
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
