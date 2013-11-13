package edu.missouristate.mote.statistics;

/**
 * The normal distribution.
 */
public final class NormalDist {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    private static final double[] A_VALUES = {5.75885480458, 2.62433121679,
        5.92885724438};
    private static final double[] B_VALUES = {-29.8213557807, 48.6959930692};
    private static final double[] C_VALUES = {-0.000000038052, 0.000398064794,
        -0.151679116635, 4.8385912808, 0.742380924027, 3.99019417011};
    private static final double[] D_VALUES = {1.00000615302, 1.98615381364,
        5.29330324926, -15.1508972451, 30.789933034};
    private static final double CON = 1.28;
    private static final double LTONE = 7.0;
    private static final double P_VALUE = 0.398942280444;
    private static final double Q_VALUE = 0.39990348504;
    private static final double R_VALUE = 0.398942280385;
    private static final double UT_ZERO = 18.66;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a NormalDist.
     */
    private NormalDist() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Calculate the cumulative density of the standard normal distribution
     * from -infinity to x.
     *
     * @param xValue endpoint of the semi-infinite interval over which the
     * integration takes place
     * @return integral of the standard normal distribution over the desired
     * interval
     */
    public static double cdf(final double xValue) {

        int up = 0;
        double zValue = xValue;

        if (zValue < 0.0) {
            up = 1;
            zValue = -zValue;
        }

        double result;
        if (LTONE < zValue && ((up == 0) || UT_ZERO < zValue)) {
            if (up == 0) {
                result = 1.0;
            } else {
                result = 0.0;
            }
            return result;
        }

        final double y = 0.5 * zValue * zValue;
        if (zValue <= CON) {
            result = 0.5 - zValue * (P_VALUE - Q_VALUE * y
                    / (y + A_VALUES[0] + B_VALUES[0]
                    / (y + A_VALUES[1] + B_VALUES[1]
                    / (y + A_VALUES[2]))));
        } else {
            result = R_VALUE * Math.exp(-y)
                    / (zValue + C_VALUES[0] + D_VALUES[0]
                    / (zValue + C_VALUES[1] + D_VALUES[1]
                    / (zValue + C_VALUES[2] + D_VALUES[2]
                    / (zValue + C_VALUES[3] + D_VALUES[3]
                    / (zValue + C_VALUES[4] + D_VALUES[4]
                    / (zValue + C_VALUES[5]))))));
        }

        if (up == 0) {
            result = 1.0 - result;
        }

        return result;
    }
    
    /**
     * Calculate the probability density of the standard normal distribution.
     *
     * @param xValue x value
     * @return probability density function
     */
    public static double pdf(final double xValue) {
        final double top = Math.exp(-0.5 * xValue * xValue);
        final double bottom = Math.sqrt(2 * Math.PI);
        return top / bottom;
    }
}