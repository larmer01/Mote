package edu.missouristate.mote.statistics;

/**
 * The non-central F distribution.
 */
public final class FDist {

    private static final double EPSILON = 1.0e-4;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a FDist.
     */
    private FDist() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    private static boolean qsmall(final double x, final double sum) {
        return sum < 1e-20 || (x) < EPSILON * sum;
    }

    /**
     * Calculate the cumulative F distribution.
     *
     * @param fValue the upper limit of integration
     * @param dfNumer number of degrees of freedom for the numerator
     * @param dfDenom number of degrees of freedom for the denominator
     * @return the value of the F CDF
     */
    private static double cdf(final double fValue, final double dfNumer,
            final double dfDenom) {

        final double prod = dfNumer * fValue;
        // XX is such that the incomplete beta with parameters
        // DFD/2 and DFN/2 evaluated at XX is 1 - CUM or CCUM
        final double dsum = dfDenom + prod;
        double xx = dfDenom / dsum;
        if (xx > 0.5) {
            xx = 1 - prod / dsum;
        }

        return 1 - BetaInc.eval(dfDenom * 0.5, dfNumer * 0.5, xx);
    }
    
    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * CUMFNC evaluates the cumulative non-central F distribution.
     *
     * @param f the upper limit of integration
     * @param dfNumer number of degrees of freedom for the numerator
     * @param dfDenom number of degrees of freedom for the denominator
     * @param nc the non-centrality parameter
     * @result the non-central F CDF
     */
    public static double cdf(final double fValue, final double dfNumer,
            final double dfDenom, final double nc) {

        if (fValue <= 0) {
            return 0;
        }
        if (nc <= 0) {
            return cdf(fValue, dfNumer, dfDenom);
        }
        // Calculate the central term of the poisson weighting factor
        final double xnonc = nc / 2;
        int icent = (int) xnonc;
        if (icent == 0) {
            icent = 1;
        }
        // Compute central weight term
        final double term1 = (double) (icent + 1);
        final double centwt = Math.exp(-xnonc + icent * Math.log(xnonc)
                - Gamma.evalLog(term1));
        // Compute central incomplete beta term
        // Assure that minimum of arg to beta and 1 - arg is computed accurately
        final double prod = dfNumer * fValue;
        final double dsum = dfDenom + prod;
        double xx, yy = dfDenom / dsum;
        if (yy > 0.5) {
            xx = prod / dsum;
            yy = 1 - xx;
        } else {
            xx = 1 - yy;
        }
        final double term2 = dfNumer * 0.5 + icent;
        final double term3 = dfDenom * 0.5;
        double betdn = BetaInc.eval(term2, term3, xx);
        double adn = dfNumer / 2 + icent;
        double aup = adn;
        final double bValue = dfDenom / 2;
        double betup = betdn;
        double sum = centwt * betdn;
        // Now sum terms backward from icent until convergence or all done
        double xmult = centwt;
        int index = icent;
        final double term4 = adn + bValue;
        final double term5 = adn + 1;
        double dnterm = Math.exp(Gamma.evalLog(term4)
                - Gamma.evalLog(term5)
                - Gamma.evalLog(bValue) + adn * Math.log(xx) + bValue
                * Math.log(yy));

        while (!qsmall(xmult * betdn, sum) && index > 0) {

            xmult *= (index / xnonc);
            index -= 1;
            adn -= 1.0;
            dnterm = (adn + 1.0) / ((adn + bValue) * xx) * dnterm;
            betdn += dnterm;
            sum += (xmult * betdn);
        }

        index = icent + 1;
        // Now sum forwards until convergence
        xmult = centwt;
        double upterm;
        if (aup - 1 + bValue == 0) {
            upterm = Math.exp(-Gamma.evalLog(aup)
                    - Gamma.evalLog(bValue) + (aup - 1) * Math.log(xx)
                    + bValue * Math.log(yy));
        } else {
            final double term6 = aup - 1 + bValue;
            upterm = Math.exp(Gamma.evalLog(term6)
                    - Gamma.evalLog(aup)
                    - Gamma.evalLog(bValue) + (aup - 1) * Math.log(xx)
                    + bValue * Math.log(yy));
        }
        while (!qsmall(xmult * betup, sum)) {
            xmult *= (xnonc / index);
            index += 1;
            aup += 1.0;
            upterm = (aup + bValue - 2) * xx / (aup - 1) * upterm;
            betup -= upterm;
            sum += (xmult * betup);
        }
        return sum;
    }

    /*
     * Calculate the probability density of the non-central f distribution.
     * 
     * @param f the upper limit of integration
     * @param dfNumer number of degrees of freedom for the numerator
     * @param dfDenom number of degrees of freedom for the denominator
     * @param nc the non-centrality parameter
     * @result the non-central F CDF
     */
    public static double pdf(final double x, final double dfn,
            final double dfd, final double nc) {
        final double n1 = dfn;
        final double n2 = dfd;
        double term = -nc / 2 + nc * n1 * x / (2 * (n2 + n1 * x))
                + Gamma.evalLog(n1 / 2)
                + Gamma.evalLog(1 + n2 / 2);
        term -= Gamma.evalLog((n1 + n2) / 2.0);
        double Px = Math.exp(term);
        Px *= Math.pow(n1, (n1 / 2)) * Math.pow(n2, (n2 / 2)) * Math.pow(x, (n1 / 2 - 1));
        Px *= Math.pow(n2+n1*x,-(n1+n2)/2);
        Px *= Laguerre.eval(n2/2, n1/2-1, -nc*n1*x/(2.0*(n2+n1*x)));
        Px /= Beta.eval(n1/2,n2/2);
        return Px;
    }    
}