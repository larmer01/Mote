package edu.missouristate.mote.statistics;

/**
 * Methods for evaluating polynomials.
 */
public final class Polynomial {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Polynomial.
     */
    private Polynomial() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Evaluate a polynomial.
     *
     * @param xValue x value
     * @param coeffs coefficients in reverse order
     * @return evaluated polynomial
     */
    public static double eval(final double xValue, final double[] coeffs) {
        double result = coeffs[0];
        for (int index = 1; index < coeffs.length; index++) {
            result = result * xValue + coeffs[index];
        }
        return result;
    }

    /**
     * Evaluate a polynomial where the coefficient of x^n is assumed to be 1.
     *
     * @param xValue x value
     * @param coeffs coefficients in reverse order
     * @return evaluated polynomial
     */
    public static double evalOne(final double xValue, final double[] coeffs) {
        double result = 1;
        for (int index = 0; index < coeffs.length; index++) {
            result = result * xValue + coeffs[index];
        }
        return result;
    }    
}
