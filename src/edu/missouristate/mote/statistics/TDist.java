package edu.missouristate.mote.statistics;

/**
 * The non-central t distribution.
 */
public final class TDist {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Sqrt(2/pi)
    private static final double R2PI = 0.79788456080286535588;
    // Log(Sqrt(pi))
    private static final double ALNRPI = 0.57236494292470008707;
    // Maximum allowable error
    private static final double MAX_ERROR = 1.0E-10;
    // Maximum number of iterations
    private static final int MAX_ITER = 1000;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a TDist.
     */
    private TDist() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Calculate the cumulative density of the non-central t distribution.
     *
     * @param tValue Point whose cumulative probability is desired
     * @param df Number of degrees of freedom
     * @param delta Non-centrality parameter
     * @return Tail of the non-central T distribution
     * @exception ArithmeticException if failed to converge
     */
    public static double cdf(final double tValue, final double df,
            final double delta) {
        if (df <= 0.0) {
            return Double.NaN;
        }

        double del = delta;
        int negdel = 0;
        if (tValue < 0.0) {
            negdel = 1;
            del = -del;
        }
        // Initialize twin series
        double en = 1.0;
        final double x = tValue * tValue / (tValue * tValue + df);
        double value = 0.0;
//        if (x <= 0.0) {
//            value = value + (1 - NormalDist.cdf(del));
//
//            if (negdel != 0) {
//                value = 1.0 - value;
//            }
//            return value;
//        }

        final double lambda = del * del;
        double p = 0.5 * Math.exp(-0.5 * lambda);
        double q = R2PI * p * del;
        double s = 0.5 - p;
        double a = 0.5;
        final double b = 0.5 * df;
        final double rxb = Math.pow(1.0 - x, b);
        final double albeta = ALNRPI + Gamma.evalLog(b)
                - Gamma.evalLog(a + b);
        double xodd = BetaInc.eval(a, b, x);
        double godd = 2.0 * rxb * Math.exp(a * Math.log(x) - albeta);
        double xeven = 1.0 - rxb;
        double geven = b * x * rxb;
        value = p * xodd + q * xeven;

        // Repeat until convergence
        double errbd = 1.0;
        for (int iter = 0; iter < MAX_ITER; iter++) {
            a = a + 1.0;
            xodd = xodd - godd;
            xeven = xeven - geven;
            godd = godd * x * (a + b - 1.0) / a;
            geven = geven * x * (a + b - 0.5) / (a + 0.5);
            p = p * lambda / (2.0 * en);
            q = q * lambda / (2.0 * en + 1.0);
            s = s - p;
            en = en + 1.0;
            value = value + p * xodd + q * xeven;
            errbd = 2.0 * s * (xodd - godd);
            if (errbd <= MAX_ERROR) {
                break;
            }
        }
        if (errbd > MAX_ERROR) {
            throw new ArithmeticException("t value may be approximate");
        }

        value = value + (1 - NormalDist.cdf(del));
        if (negdel != 0) {
            value = 1.0 - value;
        }
        return value;
    }

    /**
     * Calculate the probability density of the non-central t distribution
     *
     * @param tValue Point whose cumulative probability is desired
     * @param df Number of degrees of freedom
     * @param nc Non-centrality parameter
     * @return Probability density of the non-central t distribution
     */
    public static double pdf(final double tValue, final double df,
            final double nc) {
        final double t2 = tValue * tValue;
        final double nct2 = nc * nc * t2;
        final double fac1 = df + t2;
        double term1 = df / 2.0 * Math.log(df) + Gamma.evalLog(df + 1);
        term1 -= df * Math.log(2) + nc * nc / 2.0 + (df / 2.0)
                * Math.log(fac1) + Gamma.evalLog(df / 2.0);
        double result = Math.exp(term1);
        final double valF = nct2 / (2 * fac1);
        term1 = Math.sqrt(2) * nc * tValue
                * Hypergeometric.eval1f1(df / 2 + 1, 1.5, valF);
        term1 /= fac1 * Gamma.eval((df + 1) / 2);
        double term2 = Hypergeometric.eval1f1((df + 1) / 2, 0.5, valF);
        term2 /= Math.sqrt(fac1) * Gamma.eval(df / 2 + 1);
        result *= term1 + term2;
        return result;
    }
}