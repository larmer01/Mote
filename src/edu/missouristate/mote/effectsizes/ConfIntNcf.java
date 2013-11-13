package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.statistics.FDist;

/**
 * Provide methods for calculating effect sizes using the non-central F
 * distribution.
 */
public final class ConfIntNcf {

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************
    /**
     * Initialize a new instance of a ConfIntNcf.
     */
    private ConfIntNcf() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Find an initial value for the non-centrality parameter that is used to
     * create a lower bound for the binary search.
     *
     * @param fValue F value
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param target target probability
     * @return non-centrality parameter
     */
    private static double estimateLowerNc(final double fValue, final double df1,
            final double df2, final double target) {
        double result = 1;
        double actual = FDist.cdf(fValue, df1, df2, result);
        int count = 0;
        while (actual < target && count < Constants.NC_MAX_ITER) {
            result -= 0.1;
            actual = FDist.cdf(fValue, df1, df2, result);
            count++;
        }
        return result;
    }

    /**
     * Find an initial value for the non-centrality parameter that is used to
     * create an upper bound for the binary search.
     *
     * @param fValue F value
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param target target probability
     * @return non-centrality parameter
     */
    private static double estimateUpperNc(final double fValue, final double df1,
            final double df2, final double target) {
        double result = 0.1;
        double actual = FDist.cdf(fValue, df1, df2, result);
        int count = 0;
        while (actual > target && count < Constants.NC_MAX_ITER) {
            result += 1;
            actual = FDist.cdf(fValue, df1, df2, result);
            count++;
        }
        return result;
    }

    /**
     * Find a value for F that is used to create a lower bound on the left edge
     * of the PDF curve.
     *
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param nc non-centrality parameter
     * @return F for the left edge of the PDF
     */
    private static double findLowerPdfF(final double df1, final double df2,
            final double nc) {
        double fValue = nc;
        double actual = FDist.pdf(fValue, df1, df2, nc);
        double fplus1 = FDist.pdf(fValue + 1, df1, df2, nc);
        while (actual > Constants.MIN_PDF_PROB || fplus1 < actual) {
            fValue -= 1;
            fplus1 = actual;
            actual = FDist.pdf(fValue, df1, df2, nc);
        }
        return fValue;
    }

    /**
     * Find a value for F that is used to create an upper bound on the right
     * edge of the PDF curve.
     *
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param nc non-centrality parameter
     * @return F for the right edge of the PDF
     */
    private static double findUpperPdfF(final double df1, final double df2,
            final double nc) {
        double fValue = nc;
        double actual = FDist.pdf(fValue, df1, df2, nc);
        double fminus1 = FDist.pdf(fValue - 1, df1, df2, nc);
        while (actual > Constants.MIN_PDF_PROB || fminus1 < actual) {
            fValue += 1;
            fminus1 = actual;
            actual = FDist.pdf(fValue, df1, df2, nc);
        }
        return fValue;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return a two-dimensional array representing the probability density
     * function for the non-central F distribution. The x values are in
     * array[0][0..n] and the y values in array[1][0..n].
     *
     * @param fValue F value
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param nc non-centrality parameter
     * @param lowerNc lower confidence interval non-centrality parameter
     * @param upperNc upper confidence interval non-centrality parameter
     * @return two-dimensional array holding the PDF
     */
    public static double[][] createPdf(final double fValue, final double df1,
            final double df2, final double nc, final double lowerNc,
            final double upperNc) {
        // Find the correct upper boundary for F
        final double upperFa = findUpperPdfF(df1, df2, upperNc);
        final double width = upperNc - lowerNc;
        final double upperF = Math.max(upperFa, fValue + width);
        // Create the PDF curve
        final int size = (int) (upperF / Constants.F_STEP) + 1;
        final double[][] result = new double[2][size];
        for (int index = 0; index < size; index++) {
            result[0][index] = index * Constants.F_STEP;
            result[1][index] = FDist.pdf(result[0][index], df1, df2, nc);
        }
        return result;
    }

    /**
     * Find the non-centrality parameter that is within PRECISION of the target
     * cumulative probability on the CDF for the specified F.
     *
     * @param fValue F
     * @param df1 numerator degrees of freedom
     * @param df2 denominator degrees of freedom
     * @param target target probability
     * @return non-centrality parameter
     */
    public static double findNonCentrality(final double fValue,
            final double df1, final double df2, final double target) {
        double lowerNc = estimateLowerNc(fValue, df1, df2, target);
        double upperNc = estimateUpperNc(fValue, df1, df2, target);
        double result = (lowerNc + upperNc) * 0.5;
        double actual = FDist.cdf(fValue, df1, df2, result);
        int count = 0;
        while (Math.abs(actual - target) > Constants.PRECISION
                && count < Constants.NC_MAX_ITER) {
            if (actual < target) {
                upperNc = result;
                result = (lowerNc + result) * 0.5;
            } else {
                lowerNc = result;
                result = (upperNc + result) * 0.5;
            }
            actual = FDist.cdf(fValue, df1, df2, result);
            count++;
        }
        return result;
    }
}