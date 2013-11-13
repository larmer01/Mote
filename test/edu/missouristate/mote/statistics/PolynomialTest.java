package edu.missouristate.mote.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Polynomial class.
 */
public class PolynomialTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.000000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class Polynomial.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Polynomial.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be private",
                Modifier.isPrivate(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Polynomial.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of eval method, of class Polynomial.
     */
    @Test
    public void testEval() {
        final double[] coeffs = {0.1, 2.3, -1.7, 6};
        assertEquals(51.1125, Polynomial.eval(-4.5, coeffs), DELTA);
    }

    /**
     * Test of evalOne method, of class Polynomial.
     */
    @Test
    public void testEvalOne() {
        final double[] coeffs = {0.1, 2.3, -1.7, 6};
        assertEquals(461.175, Polynomial.evalOne(-4.5, coeffs), DELTA);        
    }
}