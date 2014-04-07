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
        double result = -1;
        double prob = TDist.cdf(tValue, df, result);
        int count = 0;
        while (prob < target && count < Constants.NC_MAX_ITER) {
            result *= 2;
            prob = TDist.cdf(tValue, df, result);
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
        double prob = TDist.cdf(tValue, df, result);
        int count = 0;
        while (prob > target && count < Constants.NC_MAX_ITER) {
            result *= 2;
            prob = TDist.cdf(tValue, df, result);
            count++;
        }
        return result;
    }

    /**
     * Return the t value associated with the displayed edge of a PDF curve.
     *
     * @param tPeak t value at the peak of the curve
     * @param df degrees of freedom
     * @param nc non-centrality parameter
     * @param tStep search increments of t
     * @return t value at the desired edge of the PDF curve
     */
    private static double findPdfBoundary(final double tPeak, final double df,
            final double nc, final double tStep) {
        final double target = TDist.pdf(tPeak, df, nc) * Constants.MIN_PDF_PROB;
        double tValue = tPeak;
        double actual = TDist.pdf(tValue, df, nc);
        while (actual > target) {
            tValue += tStep;
            actual = TDist.pdf(tValue, df, nc);
        }
        return tValue;
    }

    /**
     * Return the t value associated with the peak of a PDF curve.
     *
     * @param df degrees of freedom
     * @param nc non-centrality parameter
     * @return t value associated with the peak of a PDF curve
     */
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
        // Find the correct upper/lower boundaries for t
        final double lowerT = findPdfBoundary(findPdfMax(df, lowerNc), df, nc,
                -Constants.T_STEP);
        final double upperT = findPdfBoundary(findPdfMax(df, upperNc), df, nc,
                Constants.T_STEP);
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
        double lowerNc = estimateLowerNc(tValue, df, target);
        double upperNc = estimateUpperNc(tValue, df, target);
        double result = (lowerNc + upperNc) * 0.5;
        double prob = TDist.cdf(tValue, df, result);
        int count = 0;
        double error = Math.abs(prob - target);
        while (error > Constants.PRECISION && count < Constants.NC_MAX_ITER) {
            if (prob < target) {
                upperNc = result;
                result = (lowerNc + result) / 2;
            } else {
                lowerNc = result;
                result = (upperNc + result) / 2;
            }
            prob = TDist.cdf(tValue, df, result);
            error = Math.abs(prob - target);
            count++;
        }
        return result;
    }
}