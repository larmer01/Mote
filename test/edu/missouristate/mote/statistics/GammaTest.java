package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Gamma class.
 */
public class GammaTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Gamma.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Gamma.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Gamma.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class Gamma.
     */
    @Test
    public void testEval() {
        assertEquals(Double.POSITIVE_INFINITY,
                Gamma.eval(Double.POSITIVE_INFINITY), DELTA);
        assertEquals(Double.NEGATIVE_INFINITY,
                Gamma.eval(Double.NEGATIVE_INFINITY), DELTA);
        assertEquals(8.6833176188118871e+36, Gamma.eval(34), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.eval(172), DELTA);
        assertEquals(3.8543707171800731e+247, Gamma.eval(144), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.eval(-34), DELTA);
        assertEquals(6.8841366491687712e-40, Gamma.eval(-35.1), DELTA);
        assertEquals(-1.3299204362846685e-39, Gamma.eval(-34.6), DELTA);
        assertEquals(-2.4163319638582389e-38, Gamma.eval(-34.1), DELTA);
        assertEquals(2.6313083693369355e+35, Gamma.eval(33), DELTA);
        assertEquals(2.3632718012073548, Gamma.eval(-1.5), DELTA);
        assertEquals(-10000000000.577215, Gamma.eval(-1e-10), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.eval(-1), DELTA);
    }

    /**
     * Test of evalLog method, of class Gamma.
     */
    @Test
    public void testEvalLog() {
        assertEquals(Double.POSITIVE_INFINITY,
                Gamma.evalLog(Double.POSITIVE_INFINITY), DELTA);
        assertEquals(Double.NEGATIVE_INFINITY,
                Gamma.evalLog(Double.NEGATIVE_INFINITY), DELTA);
        assertEquals(-86.615982860880791, Gamma.evalLog(-34.1), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.evalLog(-35), DELTA);
        assertEquals(-89.515699508665904, Gamma.evalLog(-34.6), DELTA);
        assertEquals(17.502307845873887, Gamma.evalLog(12), DELTA);
        assertEquals(0.0, Gamma.evalLog(2), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.evalLog(2.6e305), DELTA);
        assertEquals(3622765576.2644868, Gamma.evalLog(2e8), DELTA);
        assertEquals(5912.1281784881639, Gamma.evalLog(1001), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.evalLog(0), DELTA);
        assertEquals(-0.12078223763524526, Gamma.evalLog(1.5), DELTA);
        assertEquals(0.78737508327386252, Gamma.evalLog(3.1), DELTA);
        assertEquals(-13.020973271011497, Gamma.evalLog(-10.1), DELTA);
    }
}