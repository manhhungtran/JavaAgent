package cz.muni.fi.pv168.project;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tran Manh Hung 433556
 */
public class MissionManagerImplTest {
    private DataSource data;
    private MissionManager manager;
    private Mission first;
    private Mission second;
    private Mission third;
    private Mission fourth;
    private Mission fifth;
    
    @Before
    public void setUp() throws SQLException {
        data = prepareDataSource();
        try(Connection connection = data.getConnection())
        {
            connection.prepareStatement
                ("CREATE TABLE Mission ("
                + "id bigint primary key generated always as identity,"
                + "description varchar(200),"
                + "start varchar(50)," 
                + "duration int,"
                + "difficulty varchar(30),"
                + "status varchar(30)"
                + ")").executeUpdate();
        }
        manager = new MissionManagerImpl(data);
        first = newMission(null, "Testing Mission.", LocalDate.now(), 500, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
        second = newMission(null, "Testing Mission.", LocalDate.now(), 5000, MissionDifficulty.HARD, MissionStatus.FAILED);
        third = newMission(null, "Testing Mission.", LocalDate.now(), 5, MissionDifficulty.EASY, MissionStatus.ONGOING);
        fourth = newMission(null, "Testing Mission.", LocalDate.now(), 50, MissionDifficulty.EASY, MissionStatus.SUCCEDED);
        fifth = newMission(null, "Testing Mission.", LocalDate.now(), 50, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
    }

    /**
     * Test of addMission method, of class MissionManagerImpl.
     */
    @Test
    public void testAddMission() {
        manager.addMission(first);
        assertNotNull("Saved mission has null ID", first.getId());
        Mission result = manager.getMission(first.getId());
        assertEquals("Retrieved mission differs from the saved one", first.getId(), result.getId());
    }
    
    /**
     *  Test illegal null arguments
     */
    @Test
    public void testsWithNull() throws IllegalArgumentException {
        try{
            manager.updateMission(null);
        }catch(IllegalArgumentException ignored) {
            //ok
        }
        
        try{
            manager.addMission(null);
        }catch(IllegalArgumentException ignored) {
            //ok
        }
                
        try{
            manager.getMissionsWithDifficulty(null);
        }catch(IllegalArgumentException ignored) {
            //ok
        }
                        
        try{
            manager.getMissionsWithStatus(null);
        }catch(IllegalArgumentException ignored) {
            //ok
        }
        
        try{
            manager.deleteMission(null);
        }catch(IllegalArgumentException ignored) {
            //ok
        }
    }

    /**
     * Test of getMission method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMission() {
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        
        Mission result;
        
        result = manager.getMission(first.getId());
        assertEquals(result, first);
        
        result = manager.getMission(second.getId());
        assertEquals(result, second);
        
        result = manager.getMission(third.getId());
        assertEquals(result, third);
   }

    /**
     * Test of getAllMissions method, of class MissionManagerImpl.
     */
    @Test
    public void testGetAllMissions() {
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        manager.addMission(fifth);
        
        List<Mission> result = manager.getAllMissions();
        
        assertTrue("Retrieved collection doesn't contain right amount od missions.", result.size() == 5);
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(first));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(second));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(third));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(fourth));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(fifth));
    }

    /**
     * Test of getMissionsWithStatus method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMissionsWithStatus() {
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        manager.addMission(fifth);
        
        List<Mission> result = manager.getMissionsWithStatus(MissionStatus.FAILED);
     
        assertTrue("Retrieved collection doesn't contain right amount od missions.", result.size() == 1);
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(second));
        
        result = manager.getMissionsWithStatus(MissionStatus.ONGOING);
        assertTrue("Retrieved collection doesn't contain right amount od missions.", result.size() == 3);
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(first));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(third));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(fifth));
    }

    /**
     * Test of getMissionsWithDifficulty method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMissionsWithDifficulty() {
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        manager.addMission(fifth);
        
        List<Mission> result = manager.getMissionsWithDifficulty(MissionDifficulty.MEDIUM);
     
        assertTrue("Retrieved collection shouldn't contain any mission", result.isEmpty());
        
        result = manager.getMissionsWithDifficulty(MissionDifficulty.CHUCKNORRIS);
        assertTrue("Retrieved collection doesn't contain right amount od missions.", result.size() == 2);
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(first));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(fifth));
    }
    
    /*
     * Test of deleteMission method
     */
    @Test
    public void testDeleteMission() {

        manager.addMission(first);
        manager.addMission(second);
        
        manager.deleteMission(first);

        assertTrue(!manager.getAllMissions().contains(first));
    }
    
    /*
     * Test of updateMission method
     */
    @Test
    public void testUpdateMisssion() {
        manager.addMission(first);
        second.setId(first.getId());
        manager.updateMission(second);
        
        Mission result = manager.getMission(first.getId());
        assertEquals(second.getId(), result.getId());
        assertEquals(second.getDescription(), result.getDescription());
        assertEquals(second.getDifficulty(), result.getDifficulty());
        assertEquals(second.getStatus(), result.getStatus());
        assertEquals(second.getStart(), result.getStart());
    }
    
    private static Mission newMission(Long id, String description, LocalDate start, int duration, MissionDifficulty difficulty, MissionStatus status) {
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
    
    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:missionmanagerimpl-test");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
    
    @After
    public void tearDown() throws SQLException {
        try(Connection connection = data.getConnection()) {
            connection.prepareStatement("DROP TABLE Mission").executeUpdate();
        }
    }
}
