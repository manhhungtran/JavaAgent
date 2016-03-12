package cz.muni.fi.pv168.project;

import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tran Manh Hung 433556
 */
public class MissionManagerImplTest {
    
    private MissionManager manager;
    private Mission first;
    private Mission second;
    private Mission third;
    private Mission fourth;
    private Mission fifth;
    
    
    @Before
    public void setUp() {
        manager = new MissionManagerImpl();
                
        first = newMission(0L, "Testing Mission.", LocalDate.now(), 500, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
        second = newMission(1L, "Testing Mission.", LocalDate.now(), 5000, MissionDifficulty.HARD, MissionStatus.FAILED);
        third = newMission(2L, "Testing Mission.", LocalDate.now(), 5, MissionDifficulty.EASY, MissionStatus.ONGOING);
        fourth = newMission(3L, "Testing Mission.", LocalDate.now(), 50, MissionDifficulty.EASY, MissionStatus.SUCCEDED);
        fifth = newMission(4L, "Testing Mission.", LocalDate.now(), 50, MissionDifficulty.CHUCKNORRIS, MissionStatus.ONGOING);
        
    }

    /**
     * Test of addMission method, of class MissionManagerImpl.
     */
    @Test
    public void testAddMission() {
        System.out.println("addMission");
        manager = new MissionManagerImpl();
        
        assertNotNull("Saved mission has null ID", first.getId());
        assertNotNull("saved grave has null Description", first.getDescription());
        assertNotNull("Saved mission has null Start", first.getDuration());
        assertNotNull("Saved mission has null Difficulty", first.getDifficulty());
        
        manager.addMission(first);
        
        Mission result = manager.getMission(0L);
        
        assertThat("Retrieved mission differs from the saved one", first, is(equalTo(result)));
        
        
    }
    
    /**
     *  Test illegal null arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testsWithNull() throws IllegalArgumentException {
        manager.updateMission(null);
        manager.addMission(null);
        manager.getMissionsWithDifficulty(null);
        manager.getMissionsWithStatus(null);
    }


    /**
     * Test of getMission method, of class MissionManagerImpl.
     */
    @Test
    public void testGetMission() {
        System.out.println("getMission");
        manager = new MissionManagerImpl();
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        
        Mission result;
        for(int i = 0; i < 4; i++) {
           
            Long id = new Long(i);
            result = manager.getMission(id);
            assertEquals("Retrieved mission id differs from the saved one", id, result.getId());
        }
        
    }

    /**
     * Test of getAllMissions method, of class MissionManagerImpl.
     */
    @Test
    public void testGetAllMissions() {
        System.out.println("getAllMissions");
        manager = new MissionManagerImpl();
        
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
        System.out.println("getMissionsWithStatus");
        manager = new MissionManagerImpl();
        
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
        System.out.println("getMissionsWithDifficulty");
        manager = new MissionManagerImpl();
        
        manager.addMission(first);
        manager.addMission(second);
        manager.addMission(third);
        manager.addMission(fourth);
        manager.addMission(fifth);
        
        List<Mission> result = manager.getMissionsWithDifficulty(MissionDifficulty.MEDIUM);
     
        assertTrue("Retrieved collection shouldn't contain any mission", !result.isEmpty());
        
        result = manager.getMissionsWithDifficulty(MissionDifficulty.CHUCKNORRIS);
        assertTrue("Retrieved collection doesn't contain right amount od missions.", result.size() == 2);
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(first));
        assertTrue("Retrieved collection doesn't contain required mission", result.contains(fifth));
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
    
}
