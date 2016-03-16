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
 * @author Filip Petrovic
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
        
        try( Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement
             ( "INSERT INTO AGENT (Alias,Status,Experience) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, agent.getAlias());
            statement.setString(2, agent.getStatus().toString());
            statement.setString(3, agent.getExperience().toString());

            int addedRows = statement.executeUpdate();
            if(addedRows < 1)
            {
                throw new DatabaseErrorException("Error: No rows were inserted when trying to insert agent: " + agent);
            }
            else if (addedRows > 1)
            {
                throw new DatabaseErrorException("Error: More rows (" + addedRows +
                                                 ") were inserted when trying to insert agent: " + agent);
            }

            ResultSet keyRS = statement.getGeneratedKeys();
            agent.setId(getKey(keyRS, agent));
        }
        catch (SQLException ex)
        {
            throw new DatabaseErrorException("Error when inserting agent: " + agent, ex);
        }
    }
    
    @Override
    public void updateAgent(Agent agent)
    {
        // Todo.
    }
    
    @Override
    public void deleteAgent(Agent agent)
    {
        // Todo.
    }
    
    @Override
    public Agent getAgent(Long id)
    {
        // Todo.
        return null;
    }
    
    @Override
    public List<Agent> getAllAgents()
    {
        // Todo.
        return null;
    }
    
    @Override
    public List<Agent> getAgentsWithExperience(AgentExperience experience)
    {
        // Todo.
        return null;
    }
    
    @Override
    public List<Agent> getAgentsWithStatus(AgentStatus status)
    {
        // Todo.
        return null;
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
}
