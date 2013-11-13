package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Beta class.
 */
public class BetaTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Beta.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Beta.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Beta.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class Beta.
     */
    @Test
    public void testEval() {
        assertEquals(-1, Beta.eval(-1, 1), DELTA);
        assertEquals(-0.66666666666666674, Beta.eval(-1.5, 1), DELTA);
        assertEquals(-0.5, Beta.eval(1, -2), DELTA);
        assertEquals(-0.39999999999999997, Beta.eval(1, -2.5), DELTA);
        assertEquals(9.9999950000025e-08, Beta.eval(1e7 + 5, 1), DELTA);
        assertEquals(1.2384861049536504e-17, Beta.eval(172, 10), DELTA);
        assertEquals(-0.2607759699619791, Beta.eval(1.2, -2.5), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.eval(-2, 2.3), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.eval(-2.1, -10), DELTA);
        assertEquals(Double.NEGATIVE_INFINITY, Beta.eval(-2, -3), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.eval(-2, -4), DELTA);
        assertEquals(8.8622692541952202e-16, Beta.eval(1e10, 1.5), DELTA); //right
    }
    
    /**
     * Test of evalLog method, of class Beta.
     */
    @Test
    public void testEvalLog() {
        assertEquals(0, Beta.evalLog(-1, 1), DELTA);
        assertEquals(-0.40546510810816427, Beta.evalLog(-1.5, 1), DELTA);
        assertEquals(-0.69314718055994529, Beta.evalLog(1, -2), DELTA);
        assertEquals(-0.91629073187415511, Beta.evalLog(1, -2.5), DELTA);
        assertEquals(-16.118096150958195, Beta.evalLog(1e7 + 5, 1), DELTA);
        assertEquals(-38.930056830268654, Beta.evalLog(172, 10), DELTA);
        assertEquals(-1.3440935928911013, Beta.evalLog(1.2, -2.5), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.evalLog(-2, 2.3), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.evalLog(-2.1, -10), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.evalLog(-2, -3), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Beta.evalLog(-2, -4), DELTA);
    }
}