package cz.muni.fi.pv168.project;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 * @author Filip Petrovic (422334)
 */
public class DatabaseUtilities
{
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
}
