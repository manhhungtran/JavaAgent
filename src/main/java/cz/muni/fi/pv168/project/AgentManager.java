package cz.muni.fi.pv168.project;

import java.util.List;

/**
 * @author Filip Petrovic
 */
public interface AgentManager
{
    /**
     * Adds new agent into collection.
     * @param agent Agent to be added.
     */
    void addAgent(Agent agent);
    
    /**
     * Updates existing agent in collection.
     * @param agent Agent that will be updated.
     */
    void updateAgent(Agent agent);
    
    /**
     * Returns existing agent with given id.
     * @param id Id of an agent to be returned.
     * @return Existing agent with given id.
     */
    Agent getAgent(Long id);
    
    /**
     * Returns list of all agents in collection.
     * @return List of all agents in collection.
     */
    List<Agent> getAllAgents();
    
    /**
     * Returns list of all agents in collection who have given experince.
     * @param experience Desired experience.
     * @return List of all agents in collection who have given experince.
     */
    List<Agent> getAgentsWithExperience(AgentExperience experience);
    
    /**
     * Returns list of all agents in collection who have given status.
     * @param status Desired status.
     * @return List of all agents in collection who have given status.
     */
    List<Agent> getAgentsWithStatus(AgentStatus status);
}
