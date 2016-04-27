package cz.muni.fi.pv168.project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * @author Tran Manh Hung (433556) 
 */
public class MissionManagerImpl implements MissionManager 
{
    
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    
    public MissionManagerImpl(DataSource dataSource) 
    {
        this.dataSource = dataSource;
        this.jdbc = new JdbcTemplate(dataSource);
    }
    
    private final RowMapper<Mission> missionMapper = (rs, rowNum) ->
            new Mission(rs.getLong("id"), 
                    rs.getString("codename"),
                    rs.getString("description"), 
                    rs.getDate("start").toLocalDate(), 
                    MissionDifficulty.valueOf(rs.getString("difficulty")), 
                    MissionStatus.valueOf(rs.getString("status"))); 
    
    @Override
    public void addMission(Mission mission) 
    {
        checkValidation(mission);
        
        if(mission.getId() != null) 
        {
            throw new IllegalArgumentException("Mission id is already set.");
        }
                
        SimpleJdbcInsert insertMission = new SimpleJdbcInsert(jdbc)
                .withTableName("Mission").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("description", mission.getDescription())
                .addValue("start", Date.valueOf(mission.getStart()))
                .addValue("difficulty", mission.getDifficulty().name())
                .addValue("status", mission.getStatus().name())
                ;
        
        Number id = insertMission.executeAndReturnKey(parameters);
        mission.setId(id.longValue());
    }

    @Override
    public void updateMission(Mission mission) 
    {
        checkValidation(mission);
        
        if (mission.getId() == null) 
        {
            throw new IllegalArgumentException("Mission id is null.");
        }
        
        int count = jdbc.update("UPDATE Mission SET description = ?, start = ?, duration = ?, difficulty = ?, status = ? WHERE id = ?",
                mission.getDescription(),
                Date.valueOf(mission.getStart()),
                mission.getDifficulty().name(),
                mission.getStatus().name(),
                mission.getId()
        );
        
        if (count == 0) 
        {
            throw new EntityNotFoundException("Mission " + mission + " was not found in database!");
        } 
        else if (count != 1) 
        {
            throw new DatabaseErrorException("Invalid updated rows count detected (one row should be updated): " + count);
        }
    }

    @Override
    public Mission getMission(Long id) 
    {
       return jdbc.queryForObject("SELECT * FROM Mission WHERE id=?", missionMapper, id);
    }

    @Override
    public List<Mission> getAllMissions() 
    {
        return jdbc.query("SELECT * FROM Mission", missionMapper);
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
    public void deleteMission(Long id) 
    {
        if (id == null) 
        {
            throw new IllegalArgumentException("Mission id is null.");
        }
        jdbc.update("DELETE FROM Mission WHERE id=?", id);       
    }
    
    private void checkValidation(Mission mission) 
    { 
        if(mission == null) 
        {
            throw new IllegalArgumentException("Mission is null.");
        }
        
        if(mission.getStart() == null)
        {
            throw new IllegalArgumentException("Start date is null.");
        }
        
        if(mission.getDifficulty() == null)
        {
            throw new IllegalArgumentException("Difficulty is null.");
        }
        
        if(mission.getStatus() == null)
        {
            throw new IllegalArgumentException("Status is null.");
        }
    }

    private Mission getMissionFromSet(ResultSet set) throws SQLException 
    {
        Mission mission = new Mission();
        mission.setId(set.getLong("id"));
        mission.setDescription(set.getString("description"));
        mission.setDifficulty(MissionDifficulty.valueOf(set.getString("difficulty")));
        mission.setStatus(MissionStatus.valueOf(set.getString("status")));
        mission.setStart(set.getDate("start").toLocalDate());
        return mission;
    }
}
