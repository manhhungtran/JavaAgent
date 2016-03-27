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
    public void deleteAssignment(Assignment assignment)
    {
        if(assignment == null)
        {
            throw new IllegalArgumentException("Assignment is null.");
        }
        if(assignment.getId() == null)
        {
            throw new IllegalArgumentException("Assignment id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
            ("DELETE FROM Assignment WHERE id = ?"))
        {
            statement.setLong(1, assignment.getId());

            int deletedRows = statement.executeUpdate();
            if(deletedRows == 0)
            {
                throw new EntityNotFoundException(assignment + " was not found in database.");
            }
            else if(deletedRows > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + deletedRows);
            }
        }
        catch(SQLException ex)
        {
            throw new DatabaseErrorException("Error when deleting assignment: " + assignment, ex);
        }
    }
    
    @Override
    public List<Assignment> getAllAssignments() 
    {
        try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM Assignment")) 
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
                ("SELECT * FROM Assignment WHERE mission = ?")) 
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
                ("SELECT * FROM Assignment WHERE agent = ?")) 
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
        MissionManager mission = new MissionManagerImpl(dataSource);
        AgentManager agent = new AgentManagerImpl(dataSource);
        Assignment assignment = new Assignment();
        
        assignment.setId(set.getLong("id"));
        assignment.setAgent(agent.getAgent(set.getLong("agent")));
        assignment.setMission(mission.getMission(set.getLong("mission")));
        return assignment;
    }
}
