package edu.missouristate.mote.statistics;

/**
 * Gamma function.
 */
public final class Gamma {

    // *************************************************************************
    // PUBLIC CONSTANTS
    // *************************************************************************
    public static final double MAX_GAMMA = 171.624376956302725;
    // *************************************************************************
    // PRIVATE CONSTANTS
    // *************************************************************************
    private static final double LOG_PI = 1.14472988584940017414;
    private static final double LOG_SQRT_2PI = 0.91893853320467274178;
    private static final double MAX_LOG_GAMMA = 2.556348e305;
    private static final double[] P_COEFFS = {1.60119522476751861407E-4,
        1.19135147006586384913E-3, 1.04213797561761569935E-2,
        4.76367800457137231464E-2, 2.07448227648435975150E-1,
        4.94214826801497100753E-1, 9.99999999999999996796E-1};
    private static final double[] Q_COEFFS = {-2.31581873324120129819E-5,
        5.39605580493303397842E-4, -4.45641913851797240494E-3,
        1.18139785222060435552E-2, 3.58236398605498653373E-2,
        -2.34591795718243348568E-1, 7.14304917030273074085E-2,
        1.00000000000000000320E0};
    private static final double SQRT_PI = 2.50662827463100050242E0;
    // *************************************************************************
    // STIRLING FORMULA CONSTANTS
    // *************************************************************************
    // Polynomial coefficients for Stirling's formula for Gamma
    private static final double[] STIR_COEFFS = {7.87311395793093628397E-4,
        -2.29549961613378126380E-4, -2.68132617805781232825E-3,
        3.47222221605458667310E-3, 8.33333333333482257126E-2,};
    private static final double MAX_STIRLING = 143.01608;
    // Polynomial coefficients for Stirling's formula for log Gamma
    private static final double[] A_COEFFS = {8.11614167470508450300E-4,
        -5.95061904284301438324E-4, 7.93650340457716943945E-4,
        -2.77777777730099687205E-3, 8.33333333333331927722E-2
    };
    // Polynomial coefficients for Stirling's formula for log Gamma (2...3)
    private static final double[] B_COEFFS = {-1.37825152569120859100E3,
        -3.88016315134637840924E4, -3.31612992738871184744E5,
        -1.16237097492762307383E6, -1.72173700820839662146E6,
        -8.53555664245765465627E5
    };
    // Polynomial coefficients for Stirling's formula for log Gamma (2...3)
    private static final double[] C_COEFFS = {-3.51815701436523470549E2,
        -1.70642106651881159223E4, -2.20528590553854454839E5,
        -1.13933444367982507207E6, -2.53252307177582951285E6,
        -2.01889141433532773231E6
    };

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Gamma.
     */
    private Gamma() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return the Gamma function computed by Stirling's formula. This is valid
     * for 33 <= x <= 172.
     *
     * @param xValue x value to evaluate
     * @return Gamma function
     */
    private static double stirlingFormula(final double xValue) {
        if (xValue >= MAX_GAMMA) {
            return Double.POSITIVE_INFINITY;
        }
        double wValue = 1.0 / xValue;
        wValue = 1.0 + wValue * Polynomial.eval(wValue, STIR_COEFFS);
        double result = Math.exp(xValue);
        if (xValue > MAX_STIRLING) {    // avoid overflow in pow()
            final double vValue = Math.pow(xValue, 0.5 * xValue - 0.25);
            result = vValue * (vValue / result);
        } else {
            result = Math.pow(xValue, xValue - 0.5) / result;
        }
        return SQRT_PI * result * wValue;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return the Gamma function evaluated at x.
     *
     * @param xValue x value to evaluate
     * @return Gamma function
     */
    public static double eval(final double xValue) {

        if (Double.isInfinite(xValue)) {
            return xValue;
        }
        int sign = 1;
        double qValue = Math.abs(xValue);
        double pValue, zValue;
        if (qValue > 33.0) {
            if (xValue < 0.0) {
                pValue = Math.floor(qValue);
                if (pValue == qValue) {
                    return Double.POSITIVE_INFINITY;
                }
                if (((int) pValue & 1) == 0) {
                    sign = -1;
                }
                zValue = qValue - pValue;
                if (zValue > 0.5) {
                    pValue += 1.0;
                    zValue = qValue - pValue;
                }
                zValue = qValue * Math.sin(Math.PI * zValue);
                zValue = Math.abs(zValue);
                zValue = Math.PI / (zValue * stirlingFormula(qValue));
            } else {
                zValue = stirlingFormula(xValue);
            }
            return sign * zValue;
        }

        zValue = 1.0;
        double newX = xValue;
        while (newX >= 3.0) {
            newX -= 1.0;
            zValue *= newX;
        }

        boolean isSmall = false;
        while (newX < 0.0) {
            if (newX > -1E-9) {
                isSmall = true;
                break;
            }
            zValue /= newX;
            newX += 1.0;
        }

        while (newX < 2.0) {
            if (newX < 1.e-9) {
                isSmall = true;
                break;
            }
            zValue /= newX;
            newX += 1.0;
        }

        if (newX == 2.0) {
            return zValue;
        }

        if (isSmall) {
            if (newX == 0.0) {
                return Double.POSITIVE_INFINITY;
            } else {
                return zValue / ((1.0 + 0.5772156649015329 * newX) * newX);
            }
        } else {
            newX -= 2.0;
            pValue = Polynomial.eval(newX, P_COEFFS);
            qValue = Polynomial.eval(newX, Q_COEFFS);
            return zValue * pValue / qValue;
        }
    }

    /**
     * Return the logarithm of the Gamma function evaluated at x.
     *
     * @param xValue x value to evaluate
     * @return logarithm of the Gamma function
     */
    public static double evalLog(final double xValue) {

        if (Double.isInfinite(xValue)) {
            return xValue;
        }

        double pValue, qValue, uValue, wValue, zValue;
        if (xValue < -34.0) {
            qValue = -xValue;
            wValue = evalLog(qValue);
            pValue = Math.floor(qValue);
            if (pValue == qValue) {
                return Double.POSITIVE_INFINITY;
            }
            zValue = qValue - pValue;
            if (zValue > 0.5) {
                pValue += 1.0;
                zValue = pValue - qValue;
            }
            zValue = qValue * Math.sin(Math.PI * zValue);
            zValue = LOG_PI - Math.log(zValue) - wValue;
            return zValue;
        }

        if (xValue < 13.0) {
            zValue = 1.0;
            pValue = 0.0;
            uValue = xValue;
            while (uValue >= 3.0) {
                pValue -= 1.0;
                uValue = xValue + pValue;
                zValue *= uValue;
            }
            while (uValue < 2.0) {
                if (uValue == 0.0) {
                    return Double.POSITIVE_INFINITY;
                }
                zValue /= uValue;
                pValue += 1.0;
                uValue = xValue + pValue;
            }
            if (zValue < 0.0) {
                zValue = -zValue;
            }
            if (uValue == 2.0) {
                return Math.log(zValue);
            }
            pValue -= 2.0;
            final double newX = xValue + pValue;
            pValue = newX * Polynomial.eval(newX, B_COEFFS)
                    / Polynomial.evalOne(newX, C_COEFFS);
            return Math.log(zValue) + pValue;
        }

        if (xValue > MAX_LOG_GAMMA) {
            return Double.POSITIVE_INFINITY;
        }

        qValue = (xValue - 0.5) * Math.log(xValue) - xValue + LOG_SQRT_2PI;
        if (xValue > 1.0e8) {
            return qValue;
        }

        pValue = 1.0 / (xValue * xValue);
        if (xValue >= 1000.0) {
            qValue += ((7.9365079365079365079365e-4 * pValue
                    - 2.7777777777777777777778e-3) * pValue
                    + 0.0833333333333333333333) / xValue;
        } else {
            qValue += Polynomial.eval(pValue, A_COEFFS) / xValue;
        }
        return qValue;
    }
}