package edu.missouristate.mote.statistics;

/**
 * Confluent Hypergeometric function.
 */
public final class Hypergeometric {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Hypergeometric.
     */
    private Hypergeometric() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Calculate the power series summation for 1F1.
     *
     * @param aValue A value
     * @param bValue B value
     * @param zValue Z value
     * @return Power series summation for 1F1 and calculation error
     */
    private static double[] getPowerSeries1F1(final double aValue,
            final double bValue, final double zValue) {
        // Set up for power series summation
        double an = aValue, bn = bValue;
        double a0 = 1.0, n = 1.0, t = 1.0;
        double maxt = 0.0;
        double[] result = {1.0, 0.0};   // sum, error
        while (t > Double.MIN_VALUE) {
            // Check bn first since if both an and bn are zero it is a
            // singularity
            if (bn == 0) {
                result[0] = Double.POSITIVE_INFINITY;
                return result;
            }
            if (an == 0) {
                return result;
            }
            if (n > 200) {
                break;
            }
            final double u = zValue * (an / (bn * n));

            // Check for blowup (100% error)
            if ((Math.abs(u) > 1.0)
                    && (maxt > (Double.MAX_VALUE / Math.abs(u)))) {
                result[1] = 1.0;
                return result;
            }
            a0 *= u;
            result[0] += a0;
            t = Math.abs(a0);
            if (t > maxt) {
                maxt = t;
            }
            an += 1.0;
            bn += 1.0;
            n += 1.0;
        }

        // Estimate error due to roundoff and cancellation
        if (result[0] != 0.0) {
            maxt /= Math.abs(result[0]);
        }
        maxt *= Double.MIN_VALUE;
        result[1] = Math.abs(Double.MIN_VALUE * n + maxt);
        return result;
    }

    /**
     * Calculate the asymptotic series summation for 1F1.
     *
     * @param aValue A value
     * @param bValue B value
     * @param zValue Z value
     * @return Asymptotic series summation for 1F1 and calculation error
     */
    private static double[] getAsymptoticSeries1F1(final double aValue,
            final double bValue, final double zValue) {
        double[] result = {0.0, 0.0};
        if (zValue == 0.0) {
            result[0] = Double.POSITIVE_INFINITY;
            result[1] = 1.0;
            return result;
        }

        double temp = Math.log(Math.abs(zValue));
        double t = zValue + temp * (aValue - bValue);
        double u = -temp * aValue;
        if (bValue > 0.0) {
            temp = Gamma.evalLog(bValue);
            t += temp;
            u += temp;
        }

        double[] hyp1 = getHyperG2F0(aValue, aValue - bValue + 1,
                -1.0 / zValue, 1);
        temp = Math.exp(u) / Gamma.eval(bValue - aValue);
        hyp1[0] *= temp;
        hyp1[1] *= temp;

        double[] hyp2 = getHyperG2F0(bValue - aValue, 1.0 - aValue,
                1.0 / zValue, 2);
        if (aValue < 0) {
            temp = Math.exp(t) / Gamma.eval(aValue);
        } else {
            temp = Math.exp(t - Gamma.evalLog(aValue));
        }
        hyp2[0] *= temp;
        hyp2[1] *= temp;

        if (zValue < 0.0) {
            result[0] = hyp1[0];
        } else {
            result[0] = hyp2[0];
        }

        result[1] = Math.abs(hyp1[1]) + Math.abs(hyp2[1]);
        if (bValue < 0.0) {
            temp = Gamma.eval(bValue);
            result[0] *= temp;
            result[1] *= Math.abs(temp);
        }
        if (result[0] != 0.0) {
            result[1] /= Math.abs(result[0]);
        }

        // Fudge factor, since error of asymptotic formula often seems this
        // much larger than advertised
        result[1] *= 30.0;
        return result;
    }

    /**
     * Calculate the confluent hypergeometric function 2F0.
     *
     * @param aValue A value
     * @param bValue B value
     * @param zValue Z value
     * @param type Determines what converging factor to use
     * @return Confluent hypergeometric function 2F0 evaluated at (a, b, z) and
     * calculation error
     */
    private static double[] getHyperG2F0(final double aValue,
            final double bValue, final double zValue, final int type) {
        double an = aValue, bn = bValue;
        double a0 = 1.0, alast = 1.0, n = 1.0, t;
        double maxt = 0.0;
        double tlast = 1000000000;
        double[] result = {0.0, 0.0};
        boolean did_converge = false;
        do {
            if (an == 0.0 || bn == 0.0) {
                did_converge = true;
                break;
            }

            // Check for blowup
            final double u = an * (bn * zValue / n);
            if ((Math.abs(u) > 1.0)
                    && (maxt > (Double.MAX_VALUE / Math.abs(u)))) {
                result[1] = 1; //Double.MAX_VALUE;
                return result;
            }

            a0 *= u;
            t = Math.abs(a0);

            // Terminating condition for asymptotic series
            if (t > tlast) {
                break;
            }

            // The sum is one term behind
            tlast = t;
            result[0] += alast;
            alast = a0;

            if (n > 200) {
                break;
            }

            an += 1.0e0;
            bn += 1.0e0;
            n += 1.0e0;
            if (t > maxt) {
                maxt = t;
            }
        } while (t > Double.MIN_VALUE);

        // Series converged! (estimate error due to roundoff and cancellation)
        if (did_converge) {
            result[0] += a0;
            result[1] = Math.abs(Double.MIN_VALUE * (n + maxt));
            return result;
        } // Series did not converge
        else {
            n -= 1.0;
            final double z_value_inv = 1.0 / zValue;
            if (type == 1) {
                alast *= (0.5 + (0.125 + 0.25 * bValue - 0.5 * aValue
                        + 0.25 * z_value_inv - 0.25 * n) / z_value_inv);
            } else if (type == 2) {
                alast *= 2.0 / 3.0 - bValue + 2.0 * aValue + z_value_inv - n;
            }

            // Estimate error due to roundoff, cancellation, and nonconvergence
            result[1] = Double.MIN_VALUE * (n + maxt) + Math.abs(a0);
        }
        result[0] += alast;
        return result;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Calculate the confluent hypergeometric function 1F1.
     *
     * @param aValue A value
     * @param bValue B value
     * @param zValue z value
     * @return Confluent hypergeometric function 1F1 evaluated at (a, b, z)
     */
    public static double eval1f1(final double aValue, final double bValue,
            final double zValue) {
        // See if a Kummer transformation will help
        if (Math.abs(bValue - aValue) < 0.001 * Math.abs(aValue)) {
            return (Math.exp(zValue) * eval1f1(bValue - aValue, bValue,
                    -zValue));
        }

        // Power series
        final double[] power_sum = getPowerSeries1F1(aValue, bValue, zValue);
        if (power_sum[1] < 1e-15) {
            return power_sum[0];
        }

        // Asymptotic series
        final double[] asym_sum = getAsymptoticSeries1F1(aValue, bValue,
                zValue);

        // Pick the result with less estimated error
        return asym_sum[1] < power_sum[1] ? asym_sum[0] : power_sum[0];
    }
}