package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.statistics.TDist;

/**
 * Provide methods for calculating effect sizes using the non-central t
 * distribution.
 */
public final class ConfIntNct {

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************
    /**
     * Initialize a new instance of a ConfIntNct.
     */
    private ConfIntNct() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Find an initial value for the non-centrality parameter that is used to
     * create a lower bound for the binary search.
     *
     * @param tValue t value
     * @param df degrees of freedom
     * @param target target probability
     * @return non-centrality parameter
     */
    private static double estimateLowerNc(final double tValue, final double df,
            final double target) {
        double result = 1;
        double actual = TDist.cdf(tValue, df, result);
        int count = 0;
        while (actual < target && count < Constants.NC_MAX_ITER) {
            result -= 1;
            actual = TDist.cdf(tValue, df, result);
            count++;
        }
        return result;
    }

    /**
     * Find an initial value for the non-centrality parameter that is used to
     * create an upper bound for the binary search.
     *
     * @param tValue t value
     * @param df degrees of freedom
     * @param target target probability
     * @return non-centrality parameter
     */
    private static double estimateUpperNc(final double tValue, final double df,
            final double target) {
        double result = 1;
        double actual = TDist.cdf(tValue, df, result);
        int count = 0;
        while (actual > target && count < Constants.NC_MAX_ITER) {
            result += 1;
            actual = TDist.cdf(tValue, df, result);
            count++;
        }
        return result;
    }
    
    private static double findPdfMax(final double df, final double nc) {
        // Check our direction
        double tValue = nc;
        double left = TDist.pdf(tValue, df, nc);
        double right = TDist.pdf(tValue + Constants.T_STEP, df, nc);
        // Search to the right
        while (left < right) {
            tValue += Constants.T_STEP;
            left = TDist.pdf(tValue, df, nc);
            right = TDist.pdf(tValue + Constants.T_STEP, df, nc);
        }
        // Search to the left
        while (right < left) {
            tValue -= Constants.T_STEP;
            left = TDist.pdf(tValue, df, nc);
            right = TDist.pdf(tValue + Constants.T_STEP, df, nc);
        }
        return TDist.pdf(tValue, df, nc);
    }

    /**
     * Find a value for t that is used to create a lower bound on the left edge
     * of the PDF curve.
     *
     * @param df degrees of freedom
     * @param nc non-centrality parameter
     * @return t for the left edge of the PDF
     */
    private static double findLowerPdfT(final double df, final double nc, double foo) {
        double tValue = nc;
        double actual = TDist.pdf(tValue, df, nc);
        double tplus1 = TDist.pdf(tValue + 1, df, nc);
        while (actual > foo || tplus1 < actual) {
            tValue -= 1;
            tplus1 = actual;
            actual = TDist.pdf(tValue, df, nc);
            //System.out.println("a=" + actual + ", foo=" + foo);
        }
        return tValue;
    }

    /**
     * Find a value for t that is used to create an upper bound on the right
     * edge of the PDF curve.
     *
     * @param df degrees of freedom
     * @param nc non-centrality parameter
     * @return t for the right edge of the PDF
     */
    private static double findUpperPdfT(final double df, final double nc, double foo) {
        double tValue = nc;
        double actual = TDist.pdf(tValue, df, nc);
        double tminus1 = TDist.pdf(tValue - 1, df, nc);
        while (actual > foo || tminus1 < actual) {
            tValue += 1;
            tminus1 = actual;
            actual = TDist.pdf(tValue, df, nc);
        }
        return tValue;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return a two-dimensional array representing the probability density
     * function for the non-central t distribution. The x values are in
     * array[0][0..n] and the y values in array[1][0..n].
     *
     * @param tValue t value
     * @param df degrees of freedom
     * @param nc non-centrality parameter
     * @param lowerNc lower confidence interval non-centrality parameter
     * @param upperNc upper confidence interval non-centrality parameter
     * @return two-dimensional array holding the PDF
     */
    public static double[][] createPdf(final double tValue, final double df,
            final double nc, final double lowerNc, final double upperNc) {
        
        final double max1 = findPdfMax(df, lowerNc);
        final double max2 = findPdfMax(df, upperNc);
        System.out.println("LM=" + max1 + ", RM=" + max2);
        final double max = Math.max(max1, max2);
        final double foo = max * Constants.MIN_PDF_PROB;
        // Find the correct upper/lower boundaries for t
        final double lowerTa = findLowerPdfT(df, lowerNc, foo);
        final double upperTa = findUpperPdfT(df, upperNc, foo);
        final double width = upperNc - lowerNc;
        final double lowerT = Math.min(lowerTa, tValue - width);
        final double upperT = Math.max(upperTa, tValue + width);
        // Create the PDF curve
        final int size = (int) ((upperT - lowerT) / Constants.T_STEP) + 1;
        final double[][] result = new double[2][size];
        for (int index = 0; index < size; index++) {
            result[0][index] = lowerT + index * Constants.T_STEP;
            result[1][index] = TDist.pdf(result[0][index], df, nc);
        }
        return result;
    }

    /**
     * Find the non-centrality parameter that is within PRECISION of the target
     * cumulative probability on the CDF for the specified t.
     *
     * @param tValue t
     * @param df degrees of freedom
     * @param target target probability
     * @return Non-centrality parameter
     */
    public static double findNonCentrality(final double tValue, final double df,
            final double target) {
        double lower_nc = estimateLowerNc(tValue, df, target);
        double upper_nc = estimateUpperNc(tValue, df, target);
        double result = (lower_nc + upper_nc) * 0.5;
        double actual = TDist.cdf(tValue, df, result);
        int count = 0;
        while (Math.abs(actual - target) > Constants.PRECISION
                && count < Constants.NC_MAX_ITER) {
            if (actual < target) {
                upper_nc = result;
                result = (lower_nc + result) * 0.5;
            } else {
                lower_nc = result;
                result = (upper_nc + result) * 0.5;
            }
            actual = TDist.cdf(tValue, df, result);
            count++;
        }
        return result;
    }
}