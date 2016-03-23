package cz.muni.fi.pv168.project;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tran Manh Hung 433556
 */
public class AssignmentManagerImplTest {
    
    public AssignmentManagerImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addAssigment method, of class AssigmetManagerImpl.
     */
    @Test
    public void testAddAssigment() {
        System.out.println("addAssigment");
        Assignment assigment = null;
        AssignmentManagerImpl instance = new AssignmentManagerImpl();
        instance.addAssigment(assigment);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateAssigment method, of class AssigmetManagerImpl.
     */
    @Test
    public void testUpdateAssigment() {
        System.out.println("updateAssigment");
        Assignment assigment = null;
        AssignmentManagerImpl instance = new AssignmentManagerImpl();
        instance.updateAssigment(assigment);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssigmentsForMission method, of class AssigmetManagerImpl.
     */
    @Test
    public void testGetAssigmentsForMission() {
        System.out.println("getAssigmentsForMission");
        Mission mission = null;
        AssignmentManagerImpl instance = new AssignmentManagerImpl();
        List<Assignment> expResult = null;
        List<Assignment> result = instance.getAssigmentsForMission(mission);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssigmentsForAgent method, of class AssigmetManagerImpl.
     */
    @Test
    public void testGetAssigmentsForAgent() {
        System.out.println("getAssigmentsForAgent");
        Agent agent = null;
        AssignmentManagerImpl instance = new AssignmentManagerImpl();
        List<Assignment> expResult = null;
        List<Assignment> result = instance.getAssigmentsForAgent(agent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllAssigments method, of class AssigmetManagerImpl.
     */
    @Test
    public void testGetAllAssigments() {
        System.out.println("getAllAssigments");
        AssignmentManagerImpl instance = new AssignmentManagerImpl();
        List<Assignment> expResult = null;
        List<Assignment> result = instance.getAllAssigments();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
