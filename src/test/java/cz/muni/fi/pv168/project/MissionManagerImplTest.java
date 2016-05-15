package cz.muni.fi.pv168.project;

import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Tran Manh Hung (433556)
 */
public class MissionManagerImplTest extends SetupBaseTest
{
    private MissionManager manager;
    private Mission first;
    private Mission second;
    private Mission third;
    private Mission fourth;
    private Mission fifth;
    
    @Before
    public void setUp() throws SQLException{
        dataSource = prepareDataSource("memory:missionmanagerimpl-test");
        executeSqlScript(dataSource, AssignmentManager.class.getResource("createTables.sql"));
        manager = new MissionManagerImpl(dataSource);
        first = createMission(null, "Testing Mission.", "Do something", MissionDifficulty.CHUCKNORRIS);
        second = createMission(null, "Testing Mission.", "Do something", MissionDifficulty.HARD);
        third = createMission(null, "Testing Mission.", "Do something", MissionDifficulty.EASY);
        fourth = createMission(null, "Testing Mission.", "Do something", MissionDifficulty.EASY);
        fifth = createMission(null, "Testing Mission.", "Do something", MissionDifficulty.CHUCKNORRIS);
    }

    /**
     * Test of addMission method, of class MissionManagerImpl.
     */
    @Test
    public void testAddMission() 
    {
        manager.addMission(first);
        assertNotNull("Saved mission has null ID", first.getId());
        Mission result = manager.getMission(first.getId());
        assertEquals("Retrieved mission differs from the saved one", first.getId(), result.getId());
    }
    
    /**
     *  Test illegal null arguments
     */
    @Test
    public void testsWithNull()
    {
        try
        {
            manager.updateMission(null);
        }
        catch(IllegalArgumentException ignored) 
        {
            //ok
        }
        
        try
        {
            manager.addMission(null);
        }
        catch(IllegalArgumentException ignored)
        {
            //ok
        }
                
        try
        {
            manager.getMissionsWithDifficulty(null);
        }
        catch(IllegalArgumentException ignored)
        {
            //ok
        }
                        
        
        try
        {
            manager.deleteMission(null);
        }
        catch(IllegalArgumentException ignored) 
        {
            //ok
        }
    }

    /**
     * Test of getMission method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMission()
    {
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
    public void testGetAllMissions() 
    {
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
     * Test of getMissionsWithDifficulty method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMissionsWithDifficulty() 
    {
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
    public void testDeleteMission() 
    {
        manager.addMission(first);
        manager.addMission(second);
        
        manager.deleteMission(first.getId());

        assertTrue(!manager.getAllMissions().contains(first));
    }
    
    /*
     * Test of updateMission method
     */
    @Test
    public void testUpdateMisssion()
    {
        manager.addMission(first);
        second.setId(first.getId());
        manager.updateMission(second);
        
        Mission result = manager.getMission(first.getId());
        assertEquals(second.getId(), result.getId());
        assertEquals(second.getDescription(), result.getDescription());
        assertEquals(second.getDifficulty(), result.getDifficulty());
    }
}
