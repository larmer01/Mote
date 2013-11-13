package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the BetaInc class.
 */
public class BetaIncTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class BetaInc.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = BetaInc.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                BetaInc.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class BetaInc.
     */
    @Test
    public void testEval() {
        assertEquals(0, BetaInc.eval(1, 1, -1), DELTA);
        assertEquals(0, BetaInc.eval(1, 1, 0), DELTA);
        assertEquals(1, BetaInc.eval(1, 1, 1), DELTA);
        assertEquals(0, BetaInc.eval(1, 1, 2), DELTA);
        assertEquals(0.70710678118654757, BetaInc.eval(0.5, 1, 0.5), DELTA);
        assertEquals(0.84614063894257729, BetaInc.eval(0.5, 0.6, 0.9), DELTA);
        assertEquals(0.514727830236621, BetaInc.eval(15, 2, 0.9), DELTA);
    }
}