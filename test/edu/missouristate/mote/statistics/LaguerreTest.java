package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Laguerre class.
 */
public class LaguerreTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.0000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Laguerre.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Laguerre.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Laguerre.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class Laguerre.
     */
    @Test
    public void testEval() {
        // Note: when testing against SciPy the order there is (x, n, a) instead
        // of (n, a, x) as we have it implemented here
        assertEquals(0.0, Laguerre.eval(1, 2, 3), DELTA);
        assertEquals(-1.465933749004277, Laguerre.eval(10.1, -4.5, 6.3), DELTA);
        assertEquals(2.333333333333333333, Laguerre.eval(3, 2, 1), DELTA);
        assertEquals(17.333333333333333333, Laguerre.eval(3, 4, 1), DELTA);
    }    
}