package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the TDist class.
 */
public class TDistTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.0000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class TDist.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = TDist.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                TDist.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of cdf method, of class TDist.
     */
    @Test
    public void testCdf() {
        assertEquals(Double.NaN, TDist.cdf(1, -1, 2), DELTA);
        assertEquals(0.0023163754967046105, TDist.cdf(-1, 5, 2), DELTA);
        assertEquals(0.15813564617934395, TDist.cdf(1, 5, 2), DELTA);
    }
    
    /**
     * Test of cdf method, of class TDist.
     */
    @Test(expected=ArithmeticException.class)
    public void testCdfNoConvergence() {
        assertEquals(0.47434545408534912, TDist.cdf(39, 49, 39), DELTA);
    }

    /**
     * Test of pdf method, of class TDist.
     */
    @Test
    public void testPdf() {
        assertEquals(0.0050696803835705816, TDist.pdf(-1, 5, 2), DELTA);
        assertEquals(0.23977928799676693, TDist.pdf(1, 5, 2), DELTA);
        assertEquals(0.05442313132032561, TDist.pdf(12.30717947649528, 339.8,
                14.466947078704834), DELTA);  
        assertEquals(0, TDist.pdf(13, 400, 15), DELTA);  
    }
}