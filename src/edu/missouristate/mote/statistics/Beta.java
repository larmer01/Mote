package edu.missouristate.mote.statistics;

/**
 * Beta function.
 */
public final class Beta {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    private static final double ASYMP_FACTOR = 1e6;
    private static final double MAX_LOG = 7.09782712893383996843E2;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Beta.
     */
    private Beta() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return the log of the asymptotic expansion of Beta for ln(|B(a, b)|) for
     * a > ASYMP_FACTOR*max(|b|, 1).
     *
     * @param aValue a value
     * @param bValue b value
     * @return asymptotic expansion of Beta and sign of the Gamma function
     */
    private static double logAsymptotic(final double aValue,
            final double bValue) {
        double result = Gamma.evalLog(bValue);
        result -= bValue * Math.log(aValue);
        result += bValue * (1 - bValue) / (2 * aValue);
        result += bValue * (1 - bValue) * (1 - 2 * bValue)
                / (12 * aValue * aValue);
        result += -bValue * bValue * (1 - bValue) * (1 - bValue)
                / (12 * aValue * aValue * aValue);
        return result;
    }

    /**
     * Return the log of the Beta function using the special case for a negative
     * integer argument.
     *
     * @param aValue a value
     * @param bValue b value
     * @return log of the Beta function
     */
    private static double logNegativeInt(final int a, final double b) {
        if (b == (int) b && 1 - a - b > 0) {
            return evalLog(1 - a - b, b);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Return the Beta function using the special case for a negative integer
     * argument.
     *
     * @param aValue a value
     * @param bValue b value
     * @return Beta function
     */
    private static double negativeInt(final int aValue, final double bValue) {
        if (bValue == (int) bValue && 1 - aValue - bValue > 0) {
            final int sign = ((int) bValue % 2 == 0) ? 1 : -1;
            return sign * eval(1 - aValue - bValue, bValue);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return the Beta function.
     *
     * @param aValue a value
     * @param bValue b value
     * @return Beta function
     */
    public static double eval(double aValue, double bValue) {

        if (aValue <= 0.0 && aValue == Math.floor(aValue)) {
            if (aValue == (int) aValue) {
                return negativeInt((int) aValue, bValue);
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }

        if (bValue <= 0.0 && bValue == Math.floor(bValue)) {
            if (bValue == (int) bValue) {
                return negativeInt((int) bValue, aValue);
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }

        double yValue;
        if (Math.abs(aValue) < Math.abs(bValue)) {
            yValue = aValue;
            aValue = bValue;
            bValue = yValue;
        }

        double sign = 1;
        if (Math.abs(aValue) > ASYMP_FACTOR * Math.abs(bValue)
                && aValue > ASYMP_FACTOR) {
            // Avoid loss of precision in Gamma.evalLog(a + b) -
            // Gamma.evalLog(a)
            //return sign * Math.exp(yValue);
            return Math.exp(logAsymptotic(aValue, bValue));
        }

        yValue = aValue + bValue;
        if (Math.abs(yValue) > Gamma.MAX_GAMMA
                || Math.abs(aValue) > Gamma.MAX_GAMMA
                || Math.abs(bValue) > Gamma.MAX_GAMMA) {
            yValue = Gamma.evalLog(yValue);
            double gammaSign = Math.copySign(1, yValue);
            sign *= gammaSign;
            final double gamma_b = Gamma.evalLog(bValue);
            gammaSign = Math.copySign(1, gamma_b);
            yValue = gamma_b - yValue;
            sign *= gammaSign;
            final double gamma_a = Gamma.evalLog(aValue);
            gammaSign = Math.copySign(1, gamma_a);
            yValue = gamma_a + yValue;
            sign *= gammaSign;
            if (yValue > MAX_LOG) {
                return sign > 0 ? Double.POSITIVE_INFINITY
                        : Double.NEGATIVE_INFINITY;
            }
            return sign * Math.exp(yValue);
        }

        yValue = Gamma.eval(yValue);
        if (yValue == 0.0) {
            return sign > 0 ? Double.POSITIVE_INFINITY
                    : Double.NEGATIVE_INFINITY;
        }

        if (aValue > bValue) {
            yValue = Gamma.eval(aValue) / yValue;
            yValue *= Gamma.eval(bValue);
        } else {
            yValue = Gamma.eval(bValue) / yValue;
            yValue *= Gamma.eval(aValue);
        }

        return yValue;
    }

    /**
     * Return the log of the Beta function.
     *
     * @param aValue a value
     * @param bValue b value
     * @return log of the Beta function
     */
    public static double evalLog(double aValue, double bValue) {

        if (aValue <= 0.0 && aValue == Math.floor(aValue)) {
            if (aValue == (int) aValue) {
                return logNegativeInt((int) aValue, bValue);
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }

        if (bValue <= 0.0 && bValue == Math.floor(bValue)) {
            if (bValue == (int) bValue) {
                return logNegativeInt((int) bValue, aValue);
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }

        double yValue;
        if (Math.abs(aValue) < Math.abs(bValue)) {
            yValue = aValue;
            aValue = bValue;
            bValue = yValue;
        }

        int sign = 1;
        if (Math.abs(aValue) > ASYMP_FACTOR * Math.abs(bValue)
                && aValue > ASYMP_FACTOR) {
            // Avoid loss of precision in Gamma.evalLog(a + b)
            // - Gamma.evalLog(a)
            return logAsymptotic(aValue, bValue);
        }

        yValue = aValue + bValue;
        if (Math.abs(yValue) > Gamma.MAX_GAMMA 
                || Math.abs(aValue) > Gamma.MAX_GAMMA 
                || Math.abs(bValue) > Gamma.MAX_GAMMA) {
            yValue = Gamma.evalLog(yValue);
            double gammaSign = Math.copySign(1, yValue);
            sign *= gammaSign;
            final double gamma_b = Gamma.evalLog(bValue);
            gammaSign = Math.copySign(1, gamma_b);
            yValue = gamma_b - yValue;
            sign *= gammaSign;
            final double gamma_a = Gamma.evalLog(aValue);
            gammaSign = Math.copySign(1, gamma_a);
            yValue = gamma_a + yValue;
            sign *= gammaSign;
            gammaSign = sign;
            return yValue;
        }

        yValue = Gamma.eval(yValue);
        if (yValue == 0.0) {
            return sign > 0 ? Double.POSITIVE_INFINITY
                    : Double.NEGATIVE_INFINITY;
        }

        if (aValue > bValue) {
            yValue = Gamma.eval(aValue) / yValue;
            yValue *= Gamma.eval(bValue);
        } else {
            yValue = Gamma.eval(bValue) / yValue;
            yValue *= Gamma.eval(aValue);
        }

        if (yValue < 0) {
            yValue = -yValue;
        } 
        return Math.log(yValue);
    }
}