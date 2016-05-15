package cz.muni.fi.pv168.project;

import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public class AssignmentManagerImpl implements AssignmentManager
{
    private final JdbcTemplate jdbc;
    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());
    
    private static final String SQLBASE = "SELECT assignment.id as id, assignment.status as status, assignment.start as start, " +
                                          "agent.id as aid, agent.alias as aalias, agent.experience as aexperience, " +
                                          "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.codename as mcodename " +
                                          "FROM Agent INNER JOIN Assignment ON Agent.id = agentId INNER JOIN Mission ON Mission.id = missionId";
    
    public AssignmentManagerImpl(DataSource dataSource)
    {
        if(dataSource == null)
        {
            throw new IllegalArgumentException("Data source is null.");
        }
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void addAssignment(Assignment assignment)
    {
        validateAssignment(assignment);
        
        if(assignment.getId() != null)
        {
            throw new IllegalArgumentException("Assignment id is already set.");
        }
        
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("Assignment").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("status", assignment.getStatus().toString())
            .addValue("start", Date.valueOf(assignment.getStartDate()))
            .addValue("agentId", assignment.getAgent().getId())
            .addValue("missionId", assignment.getMission().getId());
        
        Number id = insert.executeAndReturnKey(parameters);
        assignment.setId(id.longValue());
    }

    @Override
    public void updateAssignment(Assignment assignment)
    {
        validateAssignment(assignment);
        
        if(assignment.getId() == null)
        {
            throw new IllegalArgumentException("Assignment id is null.");
        }
                
        int count = jdbc.update("UPDATE Assignment SET status = ?, start = ?, agentId = ?, missionId = ? WHERE id = ?",
            assignment.getStatus().toString(),
            Date.valueOf(assignment.getStartDate()),
            assignment.getAgent().getId(),
            assignment.getMission().getId(),
            assignment.getId());
        
        if(count == 0)
        {
            throw new EntityNotFoundException(assignment + " was not found in database.");
        }
        else if(count > 1)
        {
            throw new DatabaseErrorException("Error: Invalid updated rows count detected: " + count);
        }
    }
    
    @Override
    public void deleteAssignment(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("Assignment id is null.");
        }
        
        try
        {
            int deletedRows = jdbc.update("DELETE FROM Assignment WHERE id = ?", id); 
            if(deletedRows == 0)
            {
                throw new EntityNotFoundException("Assignment with id <" + id + "> was not found in database.");
            }
            else if(deletedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + deletedRows);
            }
        }
        catch(DataAccessException | EntityNotFoundException | DatabaseErrorException ex)
        {
            logger.log(Level.SEVERE, "Error when deleting assignment with id: " + id, ex);
            throw new DatabaseErrorException("Error when deleting assignment with id: " + id, ex);
        }
    }
    
    @Override
    public Assignment getAssignment(Long id)
    {
        try
        {
          return jdbc.queryForObject(SQLBASE + " WHERE Assignment.id = ?", assignmentMapper, id);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving assignment with id: " + id, ex);
            throw new DatabaseErrorException("Error when retrieving assignment with id: " + id, ex);
        }
    }
    
    @Override
    public List<Assignment> getAllAssignments() 
    {
        try
        {
            return jdbc.query(SQLBASE, assignmentMapper);
        }
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, "Error when retrieving all assignments.", ex);
            throw new DatabaseErrorException("Error when retrieving all assignments.", ex);
        }
    }
    
    @Override
    public List<Assignment> getAssignmentsWithStatus(AssignmentStatus status)
    {
        try
        {
            return jdbc.query(SQLBASE + " WHERE status = ?", assignmentMapper, status.toString());
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving assignments with status: " + status, ex);
            throw new DatabaseErrorException("Error when retrieving assignments with status: " + status, ex);
        }
    }
    
    @Override
    public List<Assignment> getAssignmentsForAgent(Agent agent) 
    {
        try
        {
            return jdbc.query(SQLBASE + " WHERE Agent.id = ?", assignmentMapper, agent.getId());
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving assignments for agent: " + agent, ex);
            throw new DatabaseErrorException("Error when retrieving assignments for agent: " + agent, ex);
        }
    }
    
    @Override
    public List<Assignment> getAssignmentsForMission(Mission mission) 
    {
        try
        {
            return jdbc.query(SQLBASE + " WHERE Mission.id = ?", assignmentMapper, mission.getId());
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving assignments for mission: " + mission, ex);
            throw new DatabaseErrorException("Error when retrieving assignments for mission: " + mission, ex);
        }
    }
    
    private void validateAssignment(Assignment assignment)
    {
        if(assignment == null)
        {
            throw new IllegalArgumentException("Assignment is null.");
        }
        if(assignment.getStatus() == null)
        {
            throw new IllegalArgumentException("Assignment status is null.");
        }
        if(assignment.getStartDate() == null)
        {
            throw new IllegalArgumentException("Assignment start date is null.");
        }
        if(assignment.getAgent() == null)
        {
            throw new IllegalArgumentException("Assigned agent is null.");
        }
        if(assignment.getMission() == null)
        {
            throw new IllegalArgumentException("Assigned mission is null.");
        }
    }
     
    private final RowMapper<Assignment> assignmentMapper = (rs, rowNum) -> 
    {
        Agent agent = new Agent(
            rs.getLong("aid"),
            rs.getString("aalias"),
            AgentExperience.fromString(rs.getString("aexperience")));
        
        Mission mission = new Mission(
            rs.getLong("mid"),
            rs.getString("mcodename"),
            rs.getString("mdescription"),
            MissionDifficulty.fromString(rs.getString("mdifficulty")));
        
        return new Assignment(rs.getLong("id"), AssignmentStatus.fromString(rs.getString("status")), rs.getDate("start").toLocalDate(), agent, mission);
    };
}
