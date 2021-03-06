package cz.muni.fi.pv168.project;

import java.util.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Tran Manh Hung (433556) 
 */
public class MissionManagerImpl implements MissionManager 
{
    private final JdbcTemplate jdbc;
    private static final Logger logger = Logger.getLogger(MissionManagerImpl.class.getName());
    
    public MissionManagerImpl(DataSource dataSource) 
    {
        if(dataSource == null)
        {
            throw new IllegalArgumentException("Data source is null.");
        }
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void addMission(Mission mission) 
    {
        checkValidation(mission);
        
        if(mission.getId() != null) 
        {
            throw new IllegalArgumentException("Mission id is already set.");
        }
                
        SimpleJdbcInsert insertMission = new SimpleJdbcInsert(jdbc).withTableName("Mission").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("codename", mission.getCodename())
            .addValue("description", mission.getDescription())
            .addValue("difficulty", mission.getDifficulty().toString());
        
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
        
        int count = jdbc.update("UPDATE Mission SET codename = ?, description = ?, difficulty = ? WHERE id = ?",
            mission.getCodename(),
            mission.getDescription(),
            mission.getDifficulty().toString(),
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
        try
        {
            return jdbc.queryForObject("SELECT * FROM Mission WHERE id = ?", missionMapper, id);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving mission with id: " + id, ex);
            throw new DatabaseErrorException("Error when retrieving mission with id: " + id, ex);
        }
    }

    @Override
    public List<Mission> getAllMissions() 
    {
        try
        {
            return jdbc.query("SELECT * FROM Mission", missionMapper);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "Error when retrieving all missions.", ex);
            throw new DatabaseErrorException("Error when retrieving all missions.", ex);
        }
    }

    @Override
    public List<Mission> getMissionsWithDifficulty(MissionDifficulty difficulty) 
     {
        if(difficulty == null) 
        {
            throw new IllegalArgumentException("Null argument given.");
        }
        try
        {
            return jdbc.query("SELECT * FROM Mission WHERE difficulty = ?", missionMapper, difficulty.toString());
        }
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, "Error when retrieving missions.", ex);
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
         
        try
        {
            int count = jdbc.update("DELETE FROM Mission WHERE id = ?", id); 
            
            if(count == 0)
            {
                throw new EntityNotFoundException("Mission with id <" + id + "> was not found in database.");
            }
            else if(count > 1)
            {
                throw new DatabaseErrorException("Error: Invalid deleted rows count detected: " + count);
            }
        }
        catch(DataAccessException | EntityNotFoundException | DatabaseErrorException ex)
        {
            logger.log(Level.SEVERE, "Error when deleting mission with id: " + id, ex);
            throw new DatabaseErrorException("Error when deleting mission with id: " + id, ex);
        }
    }
    
    private void checkValidation(Mission mission) 
    { 
        if(mission == null) 
        {
            throw new IllegalArgumentException("Mission is null.");
        }
        if(mission.getCodename() == null)
        {
            throw new IllegalArgumentException("Mission codename is null.");
        }
        if(mission.getDescription() == null)
        {
            throw new IllegalArgumentException("Mission description is null.");
        }
        if(mission.getDifficulty() == null)
        {
            throw new IllegalArgumentException("Mission difficulty is null.");
        }
    }
    
    private final RowMapper<Mission> missionMapper = new RowMapper<Mission>() {
        @Override
        public Mission mapRow(ResultSet rs, int rowNum) throws SQLException { 
            return new Mission(
                rs.getLong("id"),
                rs.getString("codename"),
                rs.getString("description"),
                MissionDifficulty.fromString(rs.getString("difficulty")));
        }
    }; 
    
}
