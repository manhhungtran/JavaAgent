package cz.muni.fi.pv168.project;

import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentManagerImplTest extends SetupBaseTest
{
    private AgentManager manager;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setUp() throws SQLException
    {
        dataSource = prepareDataSource("memory:agentmanagerimpl-test");
        executeSqlScript(dataSource, AgentManager.class.getResource("createTables.sql"));
        manager = new AgentManagerImpl(dataSource);
    }
    
    @Test
    public void addNewAgent()
    {
        Agent agent = createAgent(null, "Agent", AgentExperience.NOVICE);
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
        Agent nonExistentAgent = createAgent(1L, "Non-existent agent", AgentExperience.NOVICE);
        manager.updateAgent(nonExistentAgent);
    }
    
    @Test
    public void updateAgent() throws Exception
    {
        Agent agent = createAgent(null, "Agent", AgentExperience.NOVICE);
        manager.addAgent(agent);
        
        Agent update = createAgent(agent.getId(), "Asddfd", AgentExperience.NOVICE);
        manager.updateAgent(update);
        
        assertEquals("Updated agent's alias is incorrect.", update.getAlias(), manager.getAgent(update.getId()).getAlias());
        assertEquals("Updated agent's experience is incorrect.", update.getExperience(), manager.getAgent(update.getId()).getExperience());
    }
    
    @Test
    public void deleteAgent()
    {
        Agent first = createAgent(null, "First", AgentExperience.EXPERT);
        Agent second = createAgent(null, "Second", AgentExperience.NOVICE);
        manager.addAgent(first);
        manager.addAgent(second);

        assertThat(manager.getAgent(first.getId())).isNotNull();
        assertThat(manager.getAgent(second.getId())).isNotNull();

        manager.deleteAgent(first.getId());

        assertThat(manager.getAgent(second.getId())).isNotNull();
        
        expectedException.expect(DatabaseErrorException.class);
        manager.getAgent(first.getId());
    }
    
    @Test
    public void getAllAgents()
    {
        assertThat(manager.getAllAgents().isEmpty());
        
        Agent first = createAgent(null, "First", AgentExperience.EXPERT);
        Agent second = createAgent(null, "Second", AgentExperience.NOVICE);
        Agent third = createAgent(null, "Third", AgentExperience.EXPERT);
        Agent fourth = createAgent(null, "Fourth", AgentExperience.MASTER);
        
        manager.addAgent(first);
        manager.addAgent(second);
        manager.addAgent(third);
        manager.addAgent(fourth);
        
        assertThat(manager.getAllAgents()).usingFieldByFieldElementComparator().containsOnly(first, second, third, fourth);
    }
    
    @Test
    public void getAgentsWithExperience()
    {
        Agent first = createAgent(null, "First", AgentExperience.EXPERT);
        Agent second = createAgent(null, "Second", AgentExperience.NOVICE);
        Agent third = createAgent(null, "Third", AgentExperience.EXPERT);
        Agent fourth = createAgent(null, "Fourth", AgentExperience.MASTER);
        
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
}
