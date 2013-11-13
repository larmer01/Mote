package edu.missouristate.mote.effectsizes;

/**
 * Base class for all normal tests.
 */
public abstract class AbstractNormalTest extends AbstractTest {

    /**
     * Return the number of standard deviations on either side of the measure
     * that make up the confidence interval.
     *
     * @return number of standard deviations
     */
    public abstract double getDeviations();

    /**
     * Return a two-dimensional array representing the probability density
     * function for the distribution. The x values are in array[0][0..n] and the
     * y values in array[1][0..n].
     *
     * @return probability density function
     */
    public abstract double[][] getPdf();
}