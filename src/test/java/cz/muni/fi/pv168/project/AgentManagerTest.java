package cz.muni.fi.pv168.project;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Filip Petrovic
 */
public class AgentManagerTest
{
    private AgentManager manager;
    
    @Before
    public void setUp()
    {
        manager = new AgentManagerImpl();
    }
    
    @Test
    public void addNewAgent()
    {
        Agent agent = createAgent(0L, "test", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
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
    
    @Test(expected = UnsupportedOperationException.class)
    public void updateNonExistentAgent() throws Exception
    {
        Agent nonExistentAgent = createAgent(1L, "doesn't exist", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        manager.updateAgent(nonExistentAgent);
    }
    
    @Test
    public void getAgentsWithStatus()
    {
        Agent first = createAgent(0L, "first", AgentStatus.AVAILABLE, AgentExperience.NOVICE);
        Agent second = createAgent(1L, "second", AgentStatus.ON_MISSION, AgentExperience.NOVICE);
        Agent third = createAgent(2L, "third", AgentStatus.AVAILABLE, AgentExperience.EXPERT);
        Agent fourth = createAgent(3L, "fourth", AgentStatus.RETIRED, AgentExperience.MASTER);
        
        manager.addAgent(first);
        manager.addAgent(second);
        manager.addAgent(third);
        manager.addAgent(fourth);
        
        List<Agent> availableAgents = manager.getAgentsWithStatus(AgentStatus.AVAILABLE);
        for(Agent agent : availableAgents)
        {
            if(!agent.getStatus().equals(AgentStatus.AVAILABLE))
            {
                fail("Agent with wrong status was returned.");
            }
        }
        
        List<Agent> others = manager.getAllAgents();
        others.removeAll(availableAgents);
        for(Agent agent : others)
        {
            if(agent.getStatus().equals(AgentStatus.AVAILABLE))
            {
                fail("Some agent with desired status wasn't returned.");
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
