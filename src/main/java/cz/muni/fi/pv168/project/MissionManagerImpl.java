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
 * @author Tran Manh Hung (433556) 
 */
public class MissionManagerImpl implements MissionManager 
{
    
    private DataSource dataSource;
    
    public MissionManagerImpl(DataSource data) 
    {
        this.dataSource = data;
    }
    
    @Override
    public void addMission(Mission mission) 
    {
        checkValidation(mission);
        
        if(mission.getId() != null) 
        {
            throw new IllegalArgumentException("Mission id is already set.");
        }
        
        try (Connection connection = dataSource.getConnection(); 
                PreparedStatement statement = connection.prepareStatement
        ("INSERT INTO Mission (description, start, duration, difficulty, status) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) 
        {
            
            statement.setString(1, mission.getDescription());
            statement.setString(2, mission.getStart().toString());
            statement.setInt(3, mission.getDuration());
            statement.setString(4, mission.getDifficulty().name());
            statement.setString(5, mission.getStatus().name());

            int count = statement.executeUpdate();
            if(count != 1) 
            {
                throw new DatabaseErrorException("Error: More rows were inserted when trying to insert mission: " + mission);
            }
                    
            ResultSet keyRS = statement.getGeneratedKeys();
            mission.setId(getKey(keyRS, mission));
            
        }
        catch(SQLException ex) 
        {
           throw new DatabaseErrorException("Error when inserting mission: " + mission, ex);
        }
    }

    @Override
    public void updateMission(Mission mission) 
    {
        checkValidation(mission);
        
        if (mission.getId() == null) 
        {
            throw new IllegalArgumentException("Mission id is null.");
        }
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
        ("UPDATE Mission SET description = ?, start = ?, duration = ?, difficulty = ?, status = ? WHERE id = ?")) 
        {

            statement.setString(1, mission.getDescription());   
            statement.setString(2, mission.getStart().toString());
            statement.setInt(3, mission.getDuration());
            statement.setString(4, mission.getDifficulty().name());
            statement.setString(5, mission.getStatus().name());
            statement.setLong(6, mission.getId());

            int count = statement.executeUpdate();
            
            if (count == 0) 
            {
                throw new EntityNotFoundException("Mission " + mission + " was not found in database!");
            } 
            else if (count != 1) 
            {
                throw new DatabaseErrorException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException(
                    "Error when updating mission " + mission, ex);
        }
    }

    @Override
    public Mission getMission(Long id) 
    {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM Mission WHERE id = ?")) {

            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            if (set.next()) 
            {
                
                Mission mission = getMissionFromSet(set);
                if (set.next()) 
                {
                    throw new DatabaseErrorException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + mission + " and " + getMissionFromSet(set));
                }
                return mission;
            } 
            else 
            {
                throw new DatabaseErrorException("Mission with cannot be found in database.");
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving grave with id " + id, ex);
        }
    }

    @Override
    public List<Mission> getAllMissions() 
    {
        try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM Mission")) 
        {
            
            ResultSet set = statement.executeQuery();
            List<Mission> missionList = new ArrayList<>();

            while (set.next()) 
            {
                missionList.add(getMissionFromSet(set));
            }
            return missionList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving all missions.", ex);
        }
    }

    @Override
    public List<Mission> getMissionsWithStatus(MissionStatus status) 
    {
        
        if(status == null) 
        {
            throw new IllegalArgumentException("Null argument given.");
        }
        try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM Mission WHERE status = ?")) 
        {
            
            statement.setString(1, status.toString());
            ResultSet set = statement.executeQuery();
            
            List<Mission> missionList = new ArrayList<>();

            while (set.next()) 
            {
                missionList.add(getMissionFromSet(set));
            }
            return missionList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving missions.", ex);
        }
    }

    @Override
    public List<Mission> getMissionsWithDifficulty(MissionDifficulty difficulty) 
    {
        if(difficulty == null) 
        {
            throw new IllegalArgumentException("Null argument given.");
        }
        try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM Mission WHERE difficulty = ?")) 
        {
            statement.setString(1, difficulty.toString());
            ResultSet set = statement.executeQuery();
            List<Mission> missionList = new ArrayList<>();

            while (set.next()) 
            {
                missionList.add(getMissionFromSet(set));
            }
            return missionList;
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when retrieving missions.", ex);
        }
    }
        
    @Override
    public void deleteMission(Mission mission) 
    {
        checkValidation(mission);
        
        if (mission.getId() == null) 
        {
            throw new IllegalArgumentException("Mission id is null.");
        }
        
        try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement
                ("DELETE FROM Mission WHERE id = ?")) 
        {
            statement.setLong(1, mission.getId());
            int count = statement.executeUpdate();
            
            if(count == 0) 
            {
                throw new EntityNotFoundException("Mission" + mission + "was not found in database!");
            } 
            else if (count != 1) 
            {
                throw new DatabaseErrorException("Invalid deleted rows count detected (one row should be deleted):" + count);
            }
        }
        catch (SQLException ex) 
        {
            throw new DatabaseErrorException("Error when deleting mission.", ex);
        }        
    }
    
    private void checkValidation(Mission mission) 
    { 
        if(mission == null) 
        {
            throw new IllegalArgumentException("Mission is null.");
        }
    }
    
    private Long getKey(ResultSet keyRS, Mission mission) throws DatabaseErrorException, SQLException 
    {
        if(keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) 
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert mission: "
                                                 + mission + ", wrong column count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) 
            {
                throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert mission: " +
                                                 mission + ", multiple keys found.");
            }
            return result;
        }
        else 
        {
            throw new DatabaseErrorException("Error: Generated key retrieval failed when trying to insert mission " +
                                             mission + ", no key found.");
        }
    }

    private Mission getMissionFromSet(ResultSet set) throws SQLException 
    {
        Mission mission = new Mission();
        mission.setId(set.getLong("id"));
        mission.setDescription(set.getString("description"));
        mission.setDifficulty(MissionDifficulty.valueOf(set.getString("difficulty")));
        mission.setStatus(MissionStatus.valueOf(set.getString("status")));
        mission.setDuration(set.getInt("duration"));
        mission.setStart(set.getDate("start").toLocalDate());
        return mission;
    }
}
