package cz.muni.fi.pv168.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentManagerImpl implements AgentManager
{
    private DataSource dataSource;
    
    public AgentManagerImpl(DataSource dataSource) { this.dataSource = dataSource; }
    
    @Override
    public void addAgent(Agent agent)
    {
        if(agent == null)
        {
            throw new IllegalArgumentException("Agent is null.");
        }
        if(agent.getId() != null)
        {
            throw new IllegalArgumentException("Agent id is already set.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO Agent (alias,status,experience) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, agent.getAlias());
            statement.setString(2, agent.getStatus().name());
            statement.setString(3, agent.getExperience().name());

            int addedRows = statement.executeUpdate();
            if(addedRows == 0)
            {
                throw new DatabaseErrorException("Error: No rows were inserted when trying to insert agent: " + agent);
            }
            else if(addedRows > 1)
            {
                throw new DatabaseErrorException("Error: More rows (" + addedRows +
                                                 ") were inserted when trying to insert agent: " + agent);
            }

            ResultSet keyRS = statement.getGeneratedKeys();
            agent.setId(getKey(keyRS, agent));
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when inserting agent: " + agent, ex);
        }
    }
    
    @Override
    public void updateAgent(Agent agent)
    {
        if(agent == null)
        {
            throw new IllegalArgumentException("Agent is null.");
        }
        if(agent.getId() == null)
        {
            throw new IllegalArgumentException("Agent id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("UPDATE Agent SET alias = ?, status = ?, experience = ? WHERE id = ?"))
        {
            statement.setString(1, agent.getAlias());
            statement.setString(2, agent.getStatus().name());
            statement.setString(3, agent.getExperience().name());
            statement.setLong(4, agent.getId());

            int updatedRows = statement.executeUpdate();
            if(updatedRows == 0)
            {
                throw new EntityNotFoundException(agent + " was not found in database.");
            }
            else if(updatedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid updated rows count detected: " + updatedRows);
            }
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when updating agent: " + agent, ex);
        }
    }
    
    @Override
    public void deleteAgent(Agent agent)
    {
        if(agent == null)
        {
            throw new IllegalArgumentException("Agent is null.");
        }
        if(agent.getId() == null)
        {
            throw new IllegalArgumentException("Agent id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("DELETE FROM Agent WHERE id = ?"))
        {
            statement.setLong(1, agent.getId());

            int deletedRows = statement.executeUpdate();
            if(deletedRows == 0)
            {
                throw new EntityNotFoundException(agent + " was not found in database.");
            }
            else if(deletedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + deletedRows);
            }
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when deleting agent: " + agent, ex);
        }
    }
    
    @Override
    public Agent getAgent(Long id)
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT id,alias,status,experience FROM Agent WHERE id = ?"))
        {
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            if(set.next())
            {
                Agent agent = getAgentFromSet(set);

                if(set.next())
                {
                    throw new DatabaseErrorException("Error: More entities with same id found, source id: " +
                                                     id + ", found: " + agent + " and " + getAgentFromSet(set));
                }

                return agent;
            }
            else
            {
                throw new EntityNotFoundException("Agent with id <" + id + "> was not found in database.");
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseErrorException("Error when retrieving agent with id: " + id, ex);
        }
    }
    
    @Override
    public List<Agent> getAllAgents()
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT id,alias,status,experience FROM Agent"))
        {
            ResultSet set = statement.executeQuery();

            List<Agent> agentList = new ArrayList<>();
            while (set.next())
            {
                agentList.add(getAgentFromSet(set));
            }
            return agentList;
        }
        catch (SQLException ex)
        {
            throw new DatabaseErrorException("Error when retrieving all agents.", ex);
        }
    }
    
    @Override
    public List<Agent> getAgentsWithExperience(AgentExperience experience)
    {
        List<Agent> agentList = getAllAgents();
        List<Agent> invalidAgents = new ArrayList<>();
        
        for(Agent agent : agentList)
        {
            if(agent.getExperience() != experience)
            {
                invalidAgents.add(agent);
            }
        }
        
        agentList.removeAll(invalidAgents);
        return agentList;
    }
    
    @Override
    public List<Agent> getAgentsWithStatus(AgentStatus status)
    {
        List<Agent> agentList = getAllAgents();
        List<Agent> invalidAgents = new ArrayList<>();
        
        for(Agent agent : agentList)
        {
            if(agent.getStatus() != status)
            {
                invalidAgents.add(agent);
            }
        }
        
        agentList.removeAll(invalidAgents);
        return agentList;
    }
    
    private Long getKey(ResultSet keyRS, Agent agent) throws DatabaseErrorException, SQLException
    {
        if(keyRS.next())
        {
            if (keyRS.getMetaData().getColumnCount() != 1)
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert agent: "
                                                 + agent + ", wrong column count: " + keyRS.getMetaData().getColumnCount());
            }
            
            Long result = keyRS.getLong(1);
            
            if (keyRS.next())
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert agent: " +
                                                 agent + ", multiple keys found.");
            }
            return result;
        }
        else
        {
            throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert agent " +
                                             agent + ", no key found.");
        }
    }
    
    private Agent getAgentFromSet(ResultSet set) throws SQLException
    {
        Agent agent = new Agent();
        agent.setId(set.getLong("id"));
        agent.setAlias(set.getString("alias"));
        agent.setStatus(AgentStatus.valueOf(set.getString("status")));
        agent.setExperience(AgentExperience.valueOf(set.getString("experience")));
        return agent;
    }
}
