package cz.muni.fi.pv168.project;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Tran Manh Hung (433556), Filip Petrovic (422334)
 */
public class AssignmentManagerImplTest
{
    private AssignmentManager manager;
    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException
    {
        dataSource = prepareDataSource();
        executeSqlScript(dataSource, AssignmentManager.class.getResource("createTables.sql"));
        manager = new AssignmentManagerImpl(dataSource);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        executeSqlScript(dataSource, AssignmentManager.class.getResource("dropTables.sql"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNullAssignment() throws Exception
    {
        manager.addAssignment(null);
    }
    
    private static DataSource prepareDataSource() throws SQLException
    {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:assignmentmanagerimpl-test");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
    
    private static String[] readSqlStatements(URL url)
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
    
    private static void executeSqlScript(DataSource dataSource, URL scriptUrl) throws SQLException
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
    
    private static Assignment createAssignment(Long id, Agent agent, Mission mission)
    {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setAgent(agent);
        assignment.setMission(mission);
        
        return assignment;
    }
}
