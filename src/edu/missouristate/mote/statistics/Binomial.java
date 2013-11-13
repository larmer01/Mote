package edu.missouristate.mote.statistics;

/**
 * Binomial coefficient.
 */
public final class Binomial {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Binomial.
     */
    private Binomial() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return the binomial coefficient.
     *
     * @param nValue n value
     * @param kValue k value
     * @return binomial coefficient
     */
    public static double eval(final double nValue, final double kValue) {

        double nx;
        if (nValue < 0) {
            nx = Math.floor(nValue);
            if (nValue == nx) {
                return Double.NaN;
            }
        }

        double num;
        double kx = Math.floor(kValue);
        // Integer case: use multiplication formula for less rounding error for
        // cases where the result is an integer. This cannot be used for small
        // nonzero n due to loss of precision.
        if (kValue == kx && (Math.abs(nValue) > 1e-8 || nValue == 0)) {
            nx = Math.floor(nValue);
            if (nx == nValue && kx > nx / 2 && nx > 0) {
                // Reduce kx by symmetry
                kx = nx - kx;
            }

            if (kx >= 0 && kx < 20) {
                num = 1.0;
                double den = 1.0;
                for (int i = 1; i < 1 + (int) kx; i++) {
                    num *= i + nValue - kx;
                    den *= i;
                    if (Math.abs(num) > 1e50) {
                        num /= den;
                        den = 1.0;
                    }
                }
                return num / den;
            }
        }
        // General case
        if (kValue > 1e8 * Math.abs(nValue)) {
            // Avoid loss of precision;                                              
            num = Gamma.eval(1 + nValue) / Math.abs(kValue)
                    + Gamma.eval(1 + nValue) * nValue / (2 * Math.pow(kValue, 2)); // + ...     
            num /= Math.PI * Math.pow(Math.abs(kValue), nValue);
            if (kValue > 0) {
                kx = Math.floor(kValue);
                double dk, sgn;
                if ((int) kx == kx) {
                    dk = kValue - kx;
                    sgn = ((int) kx) % 2 == 0 ? 1 : -1;
                } else {
                    dk = kValue;
                    sgn = 1;
                }
                return num * Math.sin((dk - nValue) * Math.PI) * sgn;
            } else {
                kx = Math.floor(kValue);
                if ((int) kx == kx) {
                    return 0;
                } else {
                    return num * Math.sin(kValue * Math.PI);
                }
            }
        } else {
            return 1 / Beta.eval(1 + nValue - kValue, 1 + kValue) / (nValue + 1);
        }
    }
}