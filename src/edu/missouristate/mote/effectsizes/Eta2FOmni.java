package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Eta-squared, F test omnibus.
 */
public final class Eta2FOmni extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double ssEffect;
    private double ssTotal;
    private double dfEffect;
    private double dfError;
    private double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double measure;
    private transient double lowerMeasure;
    private transient double lowerNc;
    private transient double[][] lowerPdf;
    private transient double upperMeasure;
    private transient double upperNc;
    private transient double[][] upperPdf;
    /**
     * True if the measure should be calculated from inputs; false if from F
     */
    private transient boolean useInputs;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of an Eta2FOmni.
     */
    public Eta2FOmni() {
        super();
        reset();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return the value for eta-squared.
     *
     * @param ssEffect sum of squares of the effect
     * @param ssTotal sum of squares total
     * @return d value
     */
    private static double calcEtaSquared(final double ssEffect,
            final double ssTotal) {
        if (ssTotal != 0) {
            return ssEffect / ssTotal;
        } else {
            return 0;
        }
    }

    /**
     * Return the value for F.
     *
     * @param ms mean of the squares
     * @param mse mean squared error
     * @return F value
     */
    private static double calcF(final double ms, final double mse) {
        if (mse == 0) {
            return 0;
        } else {
            return ms / mse;
        }
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        setErrorMessage("");
        if (useInputs) {
            testStatistic = calcF(getMs(), getMse());
            measure = calcEtaSquared(ssEffect, ssTotal);
        } else {
            measure = (dfEffect * testStatistic)
                    / ((dfEffect * testStatistic) + dfError);
        }
        if (testStatistic <= 0 || Double.isNaN(testStatistic)) {
            setErrorMessage("F is zero");
            doStateChanged();
            return;
        }
        // Lower, upper non-centrality parameters
        final double alpha = 1 - confidence;
        try {
            lowerNc = ConfIntNcf.findNonCentrality(testStatistic, dfEffect,
                    dfError, 1 - (alpha * 0.5));
            upperNc = ConfIntNcf.findNonCentrality(testStatistic, dfEffect,
                    dfError, alpha * 0.5);
        } catch (ArithmeticException ex) {
            setErrorMessage(ex.getLocalizedMessage());
            doStateChanged();
            return;
        }
        // Lower, upper measure
        lowerMeasure = lowerNc / (lowerNc + dfEffect + dfError + 1);
        upperMeasure = upperNc / (upperNc + dfEffect + dfError + 1);
        // PDF curves
        lowerPdf = ConfIntNcf.createPdf(testStatistic, dfEffect, dfError,
                lowerNc, lowerNc, upperNc);
        upperPdf = ConfIntNcf.createPdf(testStatistic, dfEffect, dfError,
                upperNc, lowerNc, upperNc);
        doStateChanged();
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public String getMeasureName() {
        return Constants.ETA_LOWER + Constants.SUPERSCRIPT2;
    }

    @Override
    public String getMeasureSymbol() {
        return Constants.ETA_LOWER + Constants.SUPERSCRIPT2;
    }

    @Override
    public String getTestName() {
        return "F Test Omnibus";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "F";
    }

    @Override
    public void reset() {
        useInputs = true;
        setSsEffect(10);
        setSsTotal(27.4885);
        setDfEffect(5);
        setDfError(39);
        setConfidence(0.95);
    }

    // *************************************************************************
    // INPUT GETTER/SETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Effect Sum of Squares")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Sum of squares of the effect.")
    public double getSsEffect() {
        return ssEffect;
    }

    public void setSsEffect(final double value) {
        ssEffect = value;
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Total Sum of Squares")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Total sum of squares.")
    public double getSsTotal() {
        return ssTotal;
    }

    public void setSsTotal(final double value) {
        ssTotal = value;
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Effect Deg Free")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Degrees of freedom of the effect.")
    public double getDfEffect() {
        return dfEffect;
    }

    public void setDfEffect(final double value) {
        dfEffect = Math.max(Constants.MIN_DF, value);
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Error Deg Free")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Degrees of freedom of the error.")
    public double getDfError() {
        return dfError;
    }

    public void setDfError(final double value) {
        dfError = Math.max(Constants.MIN_DF, value);
        useInputs = true;
        calculate();
    }
    
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "F Statistic")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "F statistic. If this value is entered, "
            + "the sums of squares will clear to zero and "
            + Constants.ETA_LOWER + Constants.SUPERSCRIPT2 + " will be "
            + "calculated from the F statistic.")
    @Override
    public double getTestStatistic() {
        return testStatistic;
    }

    public void setTestStatistic(final double value) {
        testStatistic = value;
        useInputs = false;
        ssEffect = 0;
        ssTotal = 0;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "Confidence interval expressed as a "
            + "percentage in the range 0...1.")
    @Override
    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(final double value) {
        confidence = Math.min(Constants.MAX_CONFIDENCE,
                Math.max(Constants.MIN_CONFIDENCE, value));
        calculate();
    }
    
    // *************************************************************************
    // DERIVED GETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Error Sum of Squares")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "Sum of squares of the error.")
    public double getSsError() {
        return ssTotal - ssEffect;
    }
    
    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Effect Mean Squares")
    @IndexAnnotation(value = 9)
    @DescriptionAnnotation(value = "Mean of the effect squares.")
    public double getMs() {
        return ssEffect / dfEffect;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Error Mean Squares")
    @IndexAnnotation(value = 10)
    @DescriptionAnnotation(value = "Mean of the error squares.")
    public double getMse() {
        return getSsError() / dfError;
    }

    // *************************************************************************
    // RESULT GETTERS
    // *************************************************************************
    @Override
    public double getAlpha() {
        return 1 - confidence;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower " + Constants.ETA_LOWER
            + Constants.SUPERSCRIPT2)
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "Minimum value of " + Constants.ETA_LOWER
            + Constants.SUPERSCRIPT2 + " on the confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = Constants.ETA_LOWER + Constants.SUPERSCRIPT2)
    @IndexAnnotation(value = 12)
    @DescriptionAnnotation(value = Constants.ETA_LOWER + Constants.SUPERSCRIPT2
            + ".")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper " + Constants.ETA_LOWER
            + Constants.SUPERSCRIPT2)
    @IndexAnnotation(value = 13)
    @DescriptionAnnotation(value = "Maximum value of " + Constants.ETA_LOWER
            + Constants.SUPERSCRIPT2 + " on the confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.LAMBDA_LOWER + "min)")
    @IndexAnnotation(value = 14)
    @DescriptionAnnotation(value = "Non-centrality parameter ("
            + Constants.LAMBDA_LOWER + ") corresponding to the minimum value of "
            + "the measure on the confidence interval.")
    @Override
    public double getLowerNc() {
        return lowerNc;
    }

    @Override
    public double[][] getLowerPdf() {
        return lowerPdf;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper NC (" + Constants.LAMBDA_LOWER + "max)")
    @IndexAnnotation(value = 15)
    @DescriptionAnnotation(value = "Non-centrality parameter ("
            + Constants.LAMBDA_LOWER + ") corresponding to the maximum value of "
            + "the measure on the confidence interval.")
    @Override
    public double getUpperNc() {
        return upperNc;
    }

    @Override
    public double[][] getUpperPdf() {
        return upperPdf;
    }
}