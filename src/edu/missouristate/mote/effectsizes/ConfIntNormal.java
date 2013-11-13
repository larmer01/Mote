package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.statistics.NormalDist;

/**
 * Provide methods for calculating effect sizes using the normal distribution.
 */
public final class ConfIntNormal {

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************
    /**
     * Initialize a new instance of a ConfIntNormal.
     */
    private ConfIntNormal() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Find an initial value for x that is used to create a lower bound for the
     * binary search.
     *
     * @param target target probability
     * @return x value
     */
    private static double estimateLowerX(final double target) {
        double result = 0;
        double actual = NormalDist.cdf(result);
        while (actual > target) {
            result -= 1;
            actual = NormalDist.cdf(result);
        }
        return result;
    }

    /**
     * Find an initial value for x that is used to create an upper bound for the
     * binary search.
     *
     * @param target target probability
     * @return x value
     */
    private static double estimateUpperX(final double target) {
        double result = 0;
        double actual = 1 - NormalDist.cdf(result);
        while (actual < target) {
            result += 1;
            actual = NormalDist.cdf(result);
        }
        return result;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return a two-dimensional array representing the probability density
     * function for the normal distribution. The x values are in array[0][0..n]
     * and the y values in array[1][0..n].
     *
     * @return two-dimensional array holding the PDF
     */
    public static double[][] createPdf() {
        final double lowerX = findLowerPdfX();
        final double upperX = findUpperPdfX();
        final int size = (int) ((upperX - lowerX) / Constants.X_STEP) + 1;
        final double[][] result = new double[2][size];
        for (int index = 0; index < size; index++) {
            result[0][index] = lowerX + index * Constants.X_STEP;
            result[1][index] = NormalDist.pdf(result[0][index]);
        }
        return result;
    }

    /**
     * Find a value for x that is used to create a lower bound on the left edge
     * of the PDF curve.
     *
     * @return x for the left edge of the PDF
     */
    public static double findLowerPdfX() {
        double xValue = 0.0;
        double actual = NormalDist.pdf(xValue);
        while (actual > Constants.MIN_PDF_PROB) {
            xValue -= 0.1;
            actual = NormalDist.pdf(xValue);
        }
        return xValue;
    }

    /**
     * Find a value for x that is used to create an upper bound on the right
     * edge of the PDF curve.
     *
     * @return x for the right edge of the PDF
     */
    public static double findUpperPdfX() {
        double xValue = 0.0;
        double actual = NormalDist.pdf(xValue);
        while (actual > Constants.MIN_PDF_PROB) {
            xValue += 0.1;
            actual = NormalDist.pdf(xValue);
        }
        return xValue;
    }

    /**
     * Find the x value that is within PRECISION of the target cumulative
     * probability on the normal CDF.
     *
     * @return x value
     */
    public static double findX(final double target) {
        double lowerX = estimateLowerX(target);
        double upperX = estimateUpperX(target);
        double result = (lowerX + upperX) * 0.5;
        double actual = NormalDist.cdf(result);
        while (Math.abs(actual - target) > Constants.PRECISION) {
            if (actual > target) {
                upperX = result;
                result = (lowerX + result) * 0.5;
            } else {
                lowerX = result;
                result = (upperX + result) * 0.5;
            }
            actual = NormalDist.cdf(result);
        }
        return result;
    }
}