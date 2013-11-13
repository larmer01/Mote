package edu.missouristate.mote.effectsizes;

/**
 * Base class for all non-central tests.
 */
public abstract class AbstractNonCentralTest extends AbstractTest {

    /**
     * Return the lower confidence interval value of the non-centrality
     * parameter.
     *
     * @return non-centrality parameter at the lower confidence interval
     */
    public abstract double getLowerNc();

    /**
     * Return a two-dimensional array representing the lower probability density
     * function for the distribution. The x values are in array[0][0..n] and the
     * y values in array[1][0..n].
     *
     * @return upper probability density function
     */
    public abstract double[][] getLowerPdf();

    /**
     * Return the upper confidence interval value of the non-centrality
     * parameter.
     *
     * @return non-centrality parameter at the upper confidence interval
     */
    public abstract double getUpperNc();

    /**
     * Return a two-dimensional array representing the upper probability density
     * function for the distribution. The x values are in array[0][0..n] and the
     * y values in array[1][0..n].
     *
     * @return upper probability density function
     */
    public abstract double[][] getUpperPdf();
}