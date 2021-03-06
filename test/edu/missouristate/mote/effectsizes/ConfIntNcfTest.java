package edu.missouristate.mote.effectsizes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the ConfIntNcf class.
 */
public class ConfIntNcfTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************

    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.00001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Test of constructor, of class ConfIntNcf.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = ConfIntNcf.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                ConfIntNcf.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of createPdf method, of class ConfIntNcf.
     */
    @Test
    public void testCreatePdf() {
        final double[][] result = ConfIntNcf.createPdf(3, 2, 10, 4, 1, 7);
        for (int i = 0; i < result[0].length; i++) {
            //System.out.println(result[0][i] + "," + result[1][i]);
        }
        
    }

    /**
     * Test of findNonCentrality method, of class ConfIntNcf.
     */
    @Test
    public void testFindNonCentrality() {
        assertEquals(-1.2676506002282294E30,
                ConfIntNcf.findNonCentrality(3, 2, 10, 0.95), DELTA);
        assertEquals(16.956395149230957,
                ConfIntNcf.findNonCentrality(3, 2, 10, 0.05), DELTA);

        assertEquals(53.75160360336304,
                ConfIntNcf.findNonCentrality(43, 2, 150, 0.95), DELTA);
        assertEquals(122.70200276374817,
                ConfIntNcf.findNonCentrality(43, 2, 150, 0.05), DELTA);
        
        assertEquals(-1.2676506002282294E30,
                ConfIntNcf.findNonCentrality(0.6, 2, 3, 0.975), DELTA);
        assertEquals(8.329202651977539,
                ConfIntNcf.findNonCentrality(0.6, 2, 3, 0.025), DELTA);
    }
}