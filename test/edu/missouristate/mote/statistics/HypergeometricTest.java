package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Hypergeometric class.
 */
public class HypergeometricTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Hypergeometric.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Hypergeometric.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Hypergeometric.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval1f1 method, of class Hypergeometric.
     */
    @Test
    public void testEval1f1() {
        assertEquals(2.7185419861902256, Hypergeometric.eval1f1(10, 9.999, 1), DELTA);
        assertEquals(0, Hypergeometric.eval1f1(-2, 1, 2 + Math.sqrt(2)), DELTA);
        assertEquals(0, Hypergeometric.eval1f1(-2, 1, 2 - Math.sqrt(2)), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Hypergeometric.eval1f1(2, 0, 1), DELTA);
        assertEquals(17.093526623519296, Hypergeometric.eval1f1(1, 1.1, 3), DELTA);
        assertEquals(0, Hypergeometric.eval1f1(2, 1, -1), DELTA);        
        //
        // Issues
        //
        // Should evaluate to positive infinity
        assertEquals(2.9636033366827517E307, Hypergeometric.eval1f1(1, 1.1, 10000), DELTA);
    }
}