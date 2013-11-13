package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the NormalDist class.
 */
public class NormalDistTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.0000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class NormalDist.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = NormalDist.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                NormalDist.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of cdf method, of class NormalDist.
     */
    @Test
    public void testCdf() {
        assertEquals(0.066807201268858071, NormalDist.cdf(-1.5), DELTA);
        assertEquals(0.93319279873114191, NormalDist.cdf(1.5), DELTA);
        assertEquals(0.99999999999999933, NormalDist.cdf(8), DELTA);
        assertEquals(1, NormalDist.cdf(19), DELTA);
        assertEquals(6.2209605742717405e-16, NormalDist.cdf(-8), DELTA);
        assertEquals(8.5272239526309754e-81, NormalDist.cdf(-19), DELTA);
        assertEquals(0.88493032977829178, NormalDist.cdf(1.2), DELTA);
    }
    
    /**
     * Test of pdf method, of class NormalDist.
     */
    @Test
    public void testPdf() {
        assertEquals(0.19418605498321298, NormalDist.pdf(1.2), DELTA);
        assertEquals(0.0044318484119380075, NormalDist.pdf(-3), DELTA);
    }
}