package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Binomial class.
 */
public class BinomialTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Binomial.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Binomial.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Binomial.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class Binomial.
     */
    @Test
    public void testEval() {
        assertEquals(Double.NaN, Binomial.eval(-1, 2), DELTA);
        assertEquals(1.875, Binomial.eval(-1.5, 2), DELTA);
        assertEquals(45, Binomial.eval(10, 2), DELTA);
        assertEquals(77.802355994185177, Binomial.eval(10, 2.5), DELTA);
        assertEquals(0.12732395430882193, Binomial.eval(1e-9, 2.5), DELTA);
        assertEquals(66, Binomial.eval(12, 10), DELTA);
        assertEquals(8.0811721649869389e+58, Binomial.eval(10000, 19), DELTA);
        assertEquals(112837.91671096261, Binomial.eval(1e10, 0.5), DELTA); //wrong
    }
}