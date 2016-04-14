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
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public class AssignmentManagerImpl implements AssignmentManager
{
    private DataSource dataSource;
    
    public AssignmentManagerImpl(DataSource dataSource)
    {
        if(dataSource == null)
        {
            throw new IllegalArgumentException("Data source is null.");
        }
        this.dataSource = dataSource;
    }

    @Override
    public void addAssignment(Assignment assignment)
    {
        validateAssignment(assignment);
        
        if(assignment.getId() != null)
        {
            throw new IllegalArgumentException("Assignment id is already set.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO Assignment (agentId,missionId) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS))
        {
            statement.setLong(1, assignment.getAgent().getId());
            statement.setLong(2, assignment.getMission().getId());

            int addedRows = statement.executeUpdate();
            if(addedRows == 0)
            {
                throw new DatabaseErrorException("Error: No rows were inserted when trying to insert assignment: " + assignment);
            }
            else if(addedRows > 1)
            {
                throw new DatabaseErrorException("Error: More rows (" + addedRows +
                                                 ") were inserted when trying to insert assignment: " + assignment);
            }

            ResultSet keyRS = statement.getGeneratedKeys();
            assignment.setId(getKey(keyRS, assignment));
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when inserting assignment: " + assignment, ex);
        }
    }

    @Override
    public void updateAssignment(Assignment assignment)
    {
        validateAssignment(assignment);
        
        if(assignment.getId() == null)
        {
            throw new IllegalArgumentException("Assignment id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("UPDATE Assignment SET agentId = ?, missionId = ? WHERE id = ?"))
        {
            statement.setLong(1, assignment.getAgent().getId());
            statement.setLong(2, assignment.getMission().getId());
            statement.setLong(3, assignment.getId());

            int updatedRows = statement.executeUpdate();
            if(updatedRows == 0)
            {
                throw new EntityNotFoundException(assignment + " was not found in database.");
            }
            else if(updatedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid updated rows count detected: " + updatedRows);
            }
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when updating assignment: " + assignment, ex);
        }
    }
    
    @Override
    public void deleteAssignment(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("Assignment id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("DELETE FROM Assignment WHERE id = ?"))
        {
            statement.setLong(1, id);

            int deletedRows = statement.executeUpdate();
            if(deletedRows == 0)
            {
                throw new EntityNotFoundException("Assignment with id <" + id + "> was not found in database.");
            }
            else if(deletedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + deletedRows);
            }
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when deleting assignment with id: " + id, ex);
        }
    }
    
    @Override
    public Assignment getAssignment(Long id)
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT assignment.id as id, agent.id as aid, agent.alias as aalias, agent.status as astatus, agent.experience as aexperience, " +
             "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.status as mstatus, mission.duration as mduration, mission.start as mstart " +
             "FROM Agent INNER JOIN Assignment ON Agent.id = agentId " +
             "INNER JOIN Mission ON Mission.id = missionId " +
             "WHERE Assignment.id = ?"))
        {
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            if(set.next())
            {
                Assignment assignment = getAssignmentFromSet(set);

                if(set.next())
                {
                    throw new DatabaseErrorException("Error: More entities with same id found, source id: " +
                                                     id + ", found: " + assignment + " and " + getAssignmentFromSet(set));
                }

                return assignment;
            }
            else
            {
                throw new EntityNotFoundException("Assignment with id <" + id + "> was not found in database.");
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseErrorException("Error when retrieving assignment with id: " + id, ex);
        }
    }
    
    @Override
    public List<Assignment> getAllAssignments() 
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT assignment.id as id, agent.id as aid, agent.alias as aalias, agent.status as astatus, agent.experience as aexperience, " +
             "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.status as mstatus, mission.duration as mduration, mission.start as mstart " +
             "FROM Agent INNER JOIN Assignment ON Agent.id = agentId " +
             "INNER JOIN Mission ON Mission.id = missionId "))
        {
            ResultSet set = statement.executeQuery();
            List<Assignment> assignmentList = new ArrayList<>();

            while (set.next()) 
            {
                assignmentList.add(getAssignmentFromSet(set));
            }
            return assignmentList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving all assignments.", ex);
        }
    }
    
    @Override
    public List<Assignment> getAssignmentsForMission(Mission mission) 
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT assignment.id as id, agent.id as aid, agent.alias as aalias, agent.status as astatus, agent.experience as aexperience, " +
             "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.status as mstatus, mission.duration as mduration, mission.start as mstart " +
             "FROM Agent INNER JOIN Assignment ON Agent.id = agentId " +
             "INNER JOIN Mission ON Mission.id = missionId " +
             "WHERE Mission.id = ?"))
        {
            statement.setObject(1, mission.getId());
            ResultSet set = statement.executeQuery();
            List<Assignment> assignmentList = new ArrayList<>();
            
            while (set.next()) 
            {
                assignmentList.add(getAssignmentFromSet(set));
            }
            return assignmentList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving all assignments.", ex);
        }
    }

    @Override
    public List<Assignment> getAssignmentsForAgent(Agent agent) 
    {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("SELECT assignment.id as id, agent.id as aid, agent.alias as aalias, agent.status as astatus, agent.experience as aexperience, " +
             "mission.id as mid, mission.description as mdescription, mission.difficulty as mdifficulty, mission.status as mstatus, mission.duration as mduration, mission.start as mstart " +
             "FROM Agent INNER JOIN Assignment ON Agent.id = agentId " +
             "INNER JOIN Mission ON Mission.id = missionId " +
             "WHERE Agent.id = ?"))
        {
            statement.setObject(1, agent.getId());
            ResultSet set = statement.executeQuery();
            List<Assignment> assignmentList = new ArrayList<>();
            
            while (set.next()) 
            {
                assignmentList.add(getAssignmentFromSet(set));
            }
            return assignmentList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving all assignments.", ex);
        }
    }
    
    private Long getKey(ResultSet keyRS, Assignment assignment) throws DatabaseErrorException, SQLException
    {
        if(keyRS.next())
        {
            if (keyRS.getMetaData().getColumnCount() != 1)
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert assignment: "
                                                 + assignment + ", wrong column count: " + keyRS.getMetaData().getColumnCount());
            }
            
            Long result = keyRS.getLong(1);
            
            if (keyRS.next())
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert assignment: " +
                                                 assignment + ", multiple keys found.");
            }
            return result;
        }
        else
        {
            throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert assignment " +
                                             assignment + ", no key found.");
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
    
    private Assignment getAssignmentFromSet(ResultSet set) throws SQLException
    {
        Agent agent = new Agent();
        agent.setId(set.getLong("aid"));
        agent.setAlias(set.getString("aalias"));
        agent.setStatus(AgentStatus.valueOf(set.getString("astatus")));
        agent.setExperience(AgentExperience.valueOf(set.getString("aexperience")));
        
        Mission mission = new Mission();
        mission.setId(set.getLong("mid"));
        mission.setDescription(set.getString("mdescription"));
        mission.setDifficulty(MissionDifficulty.valueOf(set.getString("mdifficulty")));
        mission.setStatus(MissionStatus.valueOf(set.getString("mstatus")));
        mission.setDuration(set.getInt("mduration"));
        mission.setStart(set.getDate("mstart").toLocalDate());
        
        Assignment assignment = new Assignment();
        assignment.setId(set.getLong("id"));
        assignment.setAgent(agent);
        assignment.setMission(mission);
        
        return assignment;
    }
}
