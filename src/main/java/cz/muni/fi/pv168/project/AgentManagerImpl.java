package cz.muni.fi.pv168.project;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ...
 */
public class AgentManagerImpl implements AgentManager
{
    List<Agent> agentList = new ArrayList<>();

    @Override
    public void addAgent(Agent agent)
    {
        // ToDo.
    }
    
    @Override
    public void updateAgent(Agent agent)
    {
        // ToDo.
    }
    
    @Override
    public Agent getAgent(Long id)
    {
        // ToDo.
        return null;
    }
    
    @Override
    public List<Agent> getAllAgents()
    {
        // ToDo.
        return null;
    }
    
    @Override
    public List<Agent> getAgentsWithExperience(AgentExperience experience)
    {
        // ToDo.
        return null;
    }
    
    @Override
    public List<Agent> getAgentsWithStatus(AgentStatus status)
    {
        // ToDo.
        return null;
    }
}
