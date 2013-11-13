package edu.missouristate.mote.statistics;

/**
 * Associated Laguerre polynomials.
 */
public final class Laguerre {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a FDist.
     */
    private Laguerre() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    public static double eval(final double n, final double alpha, final double x) {
        final double top = Gamma.eval(alpha + 1 + n) / Gamma.eval(alpha + 1);
        final double bottom = Gamma.eval(n + 1);
        final double hyper = Hypergeometric.eval1f1(-n, alpha + 1, x);
        return (top / bottom) * hyper;
    }    
}