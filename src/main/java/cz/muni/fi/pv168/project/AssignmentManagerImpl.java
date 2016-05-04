package cz.muni.fi.pv168.project;

import java.util.List;
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
    
    private static final String SQLBASE = "SELECT assignment.id as id, agent.id as aid, agent.alias as aalias, agent.status as astatus, agent.experience as aexperience, " +
             "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.status as mstatus, mission.codename as mcodename, mission.start as mstart " +
             "FROM Agent INNER JOIN Assignment ON Agent.id = agentId " +
             "INNER JOIN Mission ON Mission.id = missionId";
    
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
        
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("Assignment").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("missionId", assignment.getMission().getId())
                .addValue("agentId", assignment.getAgent().getId());
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
                
        int count = jdbc.update("UPDATE Assignment SET agentId = ?, missionId = ? WHERE id = ?",
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
            int deletedRows = jdbc.update("DELETE FROM Assignment WHERE id=?", id); 
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
            throw new DatabaseErrorException("Error when deleting assignment with id: " + id, ex);
        }
    }
    
    @Override
    public Assignment getAssignment(Long id)
    {
        try
        {
          return jdbc.queryForObject(SQLBASE + " WHERE Assignment.id=?", assignmentMapper, id);
        }
        catch (Exception ex)
        {
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
            throw new DatabaseErrorException("Error when retrieving all assignments.", ex);
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
            throw new DatabaseErrorException("Error when retrieving assignment with mission: " + mission.toString(), ex);
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
            throw new DatabaseErrorException("Error when retrieving assignment with agent: " + agent.toString(), ex);
        }
    }
    
    private void validateAssignment(Assignment assignment)
    {
        if(assignment == null)
        {
            throw new IllegalArgumentException("Assignment is null.");
        }
        if(assignment.getAgent() == null)
        {
            throw new IllegalArgumentException("Agent in an assignment is null.");
        }
        if(assignment.getMission() == null)
        {
            throw new IllegalArgumentException("Mission in an assignment is null.");
        }
    }
    
     
    private final RowMapper<Assignment> assignmentMapper = (rs, rowNum) -> 
    {
        Agent agent = new Agent(
                rs.getLong("aid"),
                rs.getString("aalias"),
                AgentStatus.fromString(rs.getString("astatus")),
                AgentExperience.fromString(rs.getString("aexperience")));
        
        Mission mission = new Mission(
                rs.getLong("mid"),
                rs.getString("mcodename"),
                rs.getString("mdescription"),
                rs.getDate("mstart").toLocalDate(),
                MissionDifficulty.valueOf(rs.getString("mdifficulty")),
                MissionStatus.valueOf(rs.getString("mstatus")));
        
        return new Assignment(
                rs.getLong("id"),
                mission,
                agent);
    };
}
