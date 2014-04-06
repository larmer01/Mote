package edu.missouristate.mote.effectsizes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Eta2FOmni class.
 */
public class Eta2FOmniTest {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Maximum allowed difference between expected and actual
    private static final double DELTA = 0.0000000000001;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Test of constructor, of class ConfIntNcf.
     */
    @Test
    public void testConstructor() throws Exception {
        final Constructor[] cons = Eta2FOmni.class.getDeclaredConstructors();
        assertEquals("Should only have one constructor", 1, cons.length);
        assertTrue("Constructor should be public",
                Modifier.isPublic(cons[0].getModifiers()));
        cons[0].setAccessible(true);
        assertEquals("Constructor should return the expected type",
                Eta2FOmni.class, cons[0].newInstance().getClass());
    }

    /**
     * Test of non-centrality parameters.
     */
    @Test
    public void test01() {
        final Eta2FOmni test = new Eta2FOmni();
        test.setDfEffect(2);
        test.setDfError(2);
        test.setTestStatistic(12);
        assertEquals(-9, test.getLowerNc(), DELTA);
        assertEquals(93.82600307464601, test.getUpperNc(), DELTA);
    }
    
    /**
     * Test of non-centrality parameters.
     */
    @Test
    public void test02() {
        final Eta2FOmni test = new Eta2FOmni();
        test.setDfEffect(2);
        test.setDfError(2);
        test.setTestStatistic(13);
        //assertEquals(-9, test.getLowerNc(), DELTA);
        assertEquals(1, test.getUpperNc(), DELTA);
    }
}