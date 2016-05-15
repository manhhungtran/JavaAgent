package cz.muni.fi.pv168.project;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentManagerImpl implements AgentManager
{
    private final JdbcTemplate jdbc;
    private static final Logger logger = Logger.getLogger(AgentManagerImpl.class.getName());

    public AgentManagerImpl(DataSource dataSource)
    {
        if(dataSource == null)
        {
            throw new IllegalArgumentException("Data source is null.");
        }
        this.jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void addAgent(Agent agent)
    {
        validateAgent(agent);
        
        if(agent.getId() != null)
        {
            throw new IllegalArgumentException("Agent id is already set.");
        }
        
        SimpleJdbcInsert insertAgent = new SimpleJdbcInsert(jdbc).withTableName("Agent").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("alias", agent.getAlias())
            .addValue("experience", agent.getExperience().toString())
            .addValue("status", agent.getStatus().toString());
                
        Number id = insertAgent.executeAndReturnKey(parameters);
        agent.setId(id.longValue());
    }
    
    @Override
    public void updateAgent(Agent agent)
    {
        validateAgent(agent);
        
        if(agent.getId() == null)
        {
            throw new IllegalArgumentException("Agent id is null.");
        }
        int updatedRows = jdbc.update("UPDATE Agent SET alias = ?, status = ?, experience = ? WHERE id = ?", 
            agent.getAlias(), 
            agent.getStatus().toString(), 
            agent.getExperience().toString(), 
            agent.getId());
        
        if(updatedRows == 0)
        {
            throw new EntityNotFoundException(agent + " was not found in database.");
        }
        else if(updatedRows > 1)
        {
            throw new DatabaseErrorException("Error: Invalid updated rows count detected: " + updatedRows);
        }
    }
    
    @Override
    public void deleteAgent(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("Agent id is null.");
        }
        
        try
        {
            int deletedRows =  jdbc.update("DELETE FROM Agent WHERE id = ?", id);  
            
            if(deletedRows == 0)
            {
                throw new EntityNotFoundException("Agent with id <" + id + "> was not found in database.");
            }
            else if(deletedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + deletedRows);
            }
        }
        catch(DataAccessException | EntityNotFoundException | DatabaseErrorException ex)
        {
            logger.log(Level.SEVERE, "Error when deleting agent with id: " + id, ex);
            throw new DatabaseErrorException("Error when deleting agent with id: " + id, ex);
        }
    }
    
    @Override
    public Agent getAgent(Long id)
    {
        try
        {
            return jdbc.queryForObject("SELECT * FROM Agent WHERE id= ?", agentMapper, id);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving agent with id: " + id, ex);
            throw new DatabaseErrorException("Error when retrieving agent with id: " + id, ex);
        }
    }
    
    @Override
    public List<Agent> getAllAgents()
    {
        try
        {
            return jdbc.query("SELECT * FROM Agent", agentMapper);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving all agents.", ex);
            throw new DatabaseErrorException("Error when retrieving all agents.", ex);
        }
    }
    
    @Override
    public List<Agent> getAgentsWithExperience(AgentExperience experience)
    {
        if(experience == null)
        {
            throw new IllegalArgumentException("Null argument given.");
        }
        
        try
        {
            return jdbc.query("SELECT * FROM Agent WHERE experience = ?", agentMapper, experience.toString());
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving agents.", ex);
            throw new DatabaseErrorException("Error when retrieving agents.", ex);
        }
    }
    
    @Override
    public List<Agent> getAgentsWithStatus(AgentStatus status)
    {
        if(status == null)
        {
            throw new IllegalArgumentException("Null argument given.");
        }
        
        try
        {
            return jdbc.query("SELECT * FROM Agent WHERE status = ?", agentMapper, status.toString());
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving agents.", ex);
            throw new DatabaseErrorException("Error when retrieving agents.", ex);
        }
    }
    
    private void validateAgent(Agent agent)
    {
        if(agent == null)
        {
            throw new IllegalArgumentException("Agent is null.");
        }
        if(agent.getAlias() == null)
        {
            throw new IllegalArgumentException("Agent alias is null.");
        }
        if(agent.getStatus() == null)
        {
            throw new IllegalArgumentException("Agent status is null.");
        }
        if(agent.getExperience() == null)
        {
            throw new IllegalArgumentException("Agent experience is null.");
        }
    }
    
    private final RowMapper<Agent> agentMapper = (rs, rowNum) ->
        new Agent(
            rs.getLong("id"),
            rs.getString("alias"),
            AgentStatus.fromString(rs.getString("status")),
            AgentExperience.fromString(rs.getString("experience")));
}
