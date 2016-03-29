package cz.muni.fi.pv168.project;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;

/**
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public class SetupBaseTest {
        
    protected DataSource dataSource;
    
    @After
    public void tearDown() throws SQLException
    {
        executeSqlScript(dataSource, AssignmentManager.class.getResource("dropTables.sql"));
    }
    
    protected static DataSource prepareDataSource(String dbName) throws SQLException
    {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbName);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
    
    protected static String[] readSqlStatements(URL url)
    {
        try(InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8"))
        {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            
            while(true)
            {
                int count = reader.read(buffer);
                if(count < 0)
                {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Cannot read SQL statement: " + url, ex);
        }
    }
    
    protected static void executeSqlScript(DataSource dataSource, URL scriptUrl) throws SQLException
    {
        try(Connection connection = dataSource.getConnection())
        {
            for(String sqlStatement : readSqlStatements(scriptUrl))
            {
                if(!sqlStatement.trim().isEmpty()) 
                {
                    connection.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        }
    }
    
    protected static Agent createAgent(Long id, String alias, AgentStatus status, AgentExperience experience)
    {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setAlias(alias);
        agent.setStatus(status);
        agent.setExperience(experience);
        return agent;
    }
    
    protected static Mission createMission(Long id, String description, LocalDate start, int duration, MissionDifficulty difficulty, MissionStatus status) {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setDescription(description);
        mission.setDifficulty(difficulty);
        mission.setDescription(description);
        mission.setDuration(duration);
        mission.setStart(start);  
        mission.setStatus(status);
        return mission;
    }
    
    protected static Assignment createAssignment(Long id, Agent agent, Mission mission)
    {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setAgent(agent);
        assignment.setMission(mission);
        
        return assignment;
    }
}
