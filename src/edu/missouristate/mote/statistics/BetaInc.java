package edu.missouristate.mote.statistics;

/**
 * Incomplete Beta function.
 */
public final class BetaInc {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a BetaInc.
     */
    private BetaInc() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Continued fraction expansion one for the incomplete beta integral.
     *
     * @param aValue first parameter
     * @param bValue second parameter
     * @param xValue upper range for integration
     * @return incomplete beta integral
     */
    private static double calcFrac1(final double aValue, final double bValue,
            final double xValue) {

        final double[] kValues = new double[8];
        kValues[0] = aValue;
        kValues[1] = aValue + bValue;
        kValues[2] = aValue;
        kValues[3] = aValue + 1.0;
        kValues[4] = 1.0;
        kValues[5] = bValue - 1.0;
        kValues[6] = kValues[3];
        kValues[7] = aValue + 2.0;

        double pkm2 = 0.0;
        double qkm2 = 1.0;
        double pkm1 = 1.0;
        double qkm1 = 1.0;

        double result = 1.0;
        for (int counter = 0; counter < 100; counter++) {

            double xk = -(xValue * kValues[0] * kValues[1])
                    / (kValues[2] * kValues[3]);
            double pk = pkm1 + pkm2 * xk;
            double qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            xk = (xValue * kValues[4] * kValues[5]) / (kValues[6] * kValues[7]);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            double t, ratio = 0.0;
            if (qk != 0) {
                ratio = pk / qk;
            }
            if (ratio == 0) {
                t = 1.0;
            } else {
                t = Math.abs((result - ratio) / ratio);
                result = ratio;
            }

            if (t < Double.MIN_NORMAL) {
                break;
            }

            kValues[0] += 1.0;
            kValues[1] += 1.0;
            kValues[2] += 2.0;
            kValues[3] += 2.0;
            kValues[4] += 1.0;
            kValues[5] -= 1.0;
            kValues[6] += 2.0;
            kValues[7] += 2.0;

            if ((Math.abs(qk) + Math.abs(pk)) > Double.MAX_VALUE) {
                pkm2 /= Double.MAX_VALUE;
                pkm1 /= Double.MAX_VALUE;
                qkm2 /= Double.MAX_VALUE;
                qkm1 /= Double.MAX_VALUE;
            }
            if ((Math.abs(qk) < Double.MIN_NORMAL)
                    || (Math.abs(pk) < Double.MIN_NORMAL)) {
                pkm2 *= Double.MAX_VALUE;
                pkm1 *= Double.MAX_VALUE;
                qkm2 *= Double.MAX_VALUE;
                qkm1 *= Double.MAX_VALUE;
            }
        }
        return result;
    }

    /**
     * Continued fraction expansion two for the incomplete beta integral.
     *
     * @param aValue first parameter
     * @param bValue second parameter
     * @param xValue upper range for integration
     * @return incomplete beta integral
     */
    private static double calcFrac2(final double aValue, final double bValue,
            final double xValue) {

        final double[] kValues = new double[8];
        kValues[0] = aValue;
        kValues[1] = bValue - 1.0;
        kValues[2] = aValue;
        kValues[3] = aValue + 1.0;
        kValues[4] = 1.0;
        kValues[5] = aValue + bValue;
        kValues[6] = aValue + 1.0;
        kValues[7] = aValue + 2.0;

        double pkm2 = 0.0;
        double qkm2 = 1.0;
        double pkm1 = 1.0;
        double qkm1 = 1.0;

        final double zValue = xValue / (1.0 - xValue);
        double result = 1.0;
        for (int counter = 0; counter < 100; counter++) {
            double xk = -(zValue * kValues[0] * kValues[1])
                    / (kValues[2] * kValues[3]);
            double pk = pkm1 + pkm2 * xk;
            double qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            xk = (zValue * kValues[4] * kValues[5]) / (kValues[6] * kValues[7]);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            double t, ratio = 0.0;
            if (qk != 0) {
                ratio = pk / qk;
            }
            if (ratio == 0) {
                t = 1.0;
            } else {
                t = Math.abs((result - ratio) / ratio);
                result = ratio;
            }

            if (t < Double.MIN_NORMAL) {
                return result;
            }

            kValues[0] += 1.0;
            kValues[1] -= 1.0;
            kValues[2] += 2.0;
            kValues[3] += 2.0;
            kValues[4] += 1.0;
            kValues[5] += 1.0;
            kValues[6] += 2.0;
            kValues[7] += 2.0;

            if ((Math.abs(qk) + Math.abs(pk)) > Double.MAX_VALUE) {
                pkm2 /= Double.MAX_VALUE;
                pkm1 /= Double.MAX_VALUE;
                qkm2 /= Double.MAX_VALUE;
                qkm1 /= Double.MAX_VALUE;
            }
            if ((Math.abs(qk) < Double.MIN_NORMAL)
                    || (Math.abs(pk) < Double.MIN_NORMAL)) {
                pkm2 *= Double.MAX_VALUE;
                pkm1 *= Double.MAX_VALUE;
                qkm2 *= Double.MAX_VALUE;
                qkm1 *= Double.MAX_VALUE;
            }
        }
        return result;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Calculate the incomplete beta integral, evaluated from zero to x.
     *
     * @param aValue first parameter
     * @param bValue second parameter
     * @param xValue upper range for integration
     * @return incomplete beta integral
     */
    public static double eval(final double aValue, final double bValue,
            final double xValue) {
        double result, a, b, t, x, onemx;
        int flag;

        if ((xValue <= 0.0) || (xValue >= 1.0)) {
            if (xValue == 0.0 || xValue == 1.0) {
                return xValue;
            }
            return 0.0;
        }

        onemx = 1.0 - xValue;

        // Transformation for small a_value
        if (aValue <= 1.0) {
            result = eval(aValue + 1.0, bValue, xValue);
            t = aValue * Math.log(xValue) + bValue * Math.log(1.0 - xValue)
                    + Gamma.evalLog(aValue + bValue)
                    - Gamma.evalLog(aValue + 1.0)
                    - Gamma.evalLog(bValue);
            result += Math.exp(t);
            return result;
        }

        // See if x is greater than the mean
        if (xValue > (aValue / (aValue + bValue))) {
            flag = 1;
            a = bValue;
            b = aValue;
            t = xValue;
            x = onemx;
        } else {
            flag = 0;
            a = aValue;
            b = bValue;
            t = onemx;
            x = xValue;
        }

        // Choose expansion for optimal convergence
        result = x * (a + b - 2.0) / (a - 1.0);
        if (result < 1.0) {
            result = calcFrac1(a, b, x);
            t = b * Math.log(t);
        } else {
            result = calcFrac2(a, b, x);
            t = (b - 1.0) * Math.log(t);
        }

        t += a * Math.log(x) + Gamma.evalLog(a + b)
                - Gamma.evalLog(a) - Gamma.evalLog(b);
        t += Math.log(result / a);

        t = Math.exp(t);
        if (flag == 1) {
            t = 1.0 - t;
        }
        return t;
    }
}