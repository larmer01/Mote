package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the FDist class.
 */
public class FDistTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.0000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class FDist.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = FDist.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                FDist.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of cdf method, of class FDist.
     */
    @Test
    public void testCdf() {
        assertEquals(0, FDist.cdf(-1, 1, 1, 1), DELTA);
        assertEquals(0, FDist.cdf(-1, 3, 5, 0), DELTA);
        assertEquals(0, FDist.cdf(0, 3, 5, 0), DELTA);
        assertEquals(0.904632568359375, FDist.cdf(3, 2, 10, 0), DELTA);
        assertEquals(0.76737608199992136, FDist.cdf(2, 3, 5, 0), DELTA);
        assertEquals(0.44417823528030381, FDist.cdf(2, 3, 5, 4), DELTA);
        assertEquals(0.60689487150112897, FDist.cdf(2, 3, 5, 1.8), DELTA);
        assertEquals(0.54896565804648667, FDist.cdf(3, 2, 10, 4), DELTA);
        assertEquals(0, FDist.cdf(3, -1, 1, 1), DELTA);
    }
    
    /**
     * Test of pdf method, of class FDist.
     */
    @Test
    public void testPdf() {
        assertEquals(0.1982333055446437, FDist.pdf(2, 3, 5, 4), DELTA);
        assertEquals(0.00984128, FDist.pdf(6.1, 10, 7, 2.3), DELTA);
    }
}