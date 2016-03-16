package cz.muni.fi.pv168.project;

import java.util.List;

/**
 * @author Filip Petrovic
 */
public interface AgentManager
{
    /**
     * Adds new agent.
     * @param agent Agent to be added.
     * @throws IllegalArgumentException when agent is null or agent id is already set.
     * @throws DatabaseErrorException when database error occurs.
     */
    void addAgent(Agent agent);
    
    /**
     * Updates existing agent.
     * @param agent Agent that will be updated.
     * @throws IllegalArgumentException when agent is null or agent id is null.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when agent with given id doesn't exist.
     */
    void updateAgent(Agent agent);
    
    /**
     * Deletes existing agent.
     * @param agent Agent that will be deleted.
     * @throws IllegalArgumentException when agent is null or agent id is null.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when agent with given id doesn't exist.
     */
    void deleteAgent(Agent agent);
    
    /**
     * Returns existing agent with given id.
     * @param id Id of an agent to be returned.
     * @return Existing agent with given id.
     * @throws DatabaseErrorException when database error occurs.
     * @throws EntityNotFoundException when agent with given id doesn't exist.
     */
    Agent getAgent(Long id);
    
    /**
     * Returns list of all agents.
     * @return List of all agents.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Agent> getAllAgents();
    
    /**
     * Returns list of all agents with given experience.
     * @param experience Desired experience.
     * @return List of all agents with given experience.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Agent> getAgentsWithExperience(AgentExperience experience);
    
    /**
     * Returns list of all agents with given status.
     * @param status Desired status.
     * @return List of all agents with given status.
     * @throws DatabaseErrorException when database error occurs.
     */
    List<Agent> getAgentsWithStatus(AgentStatus status);
}
