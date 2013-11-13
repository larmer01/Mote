package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Cohen's d, r.
 */
public final class CohenDR extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double df;
    private double rValue;
    private double size;
    // *************************************************************************
    // DERIVED FIELDS
    // *************************************************************************
    private transient double rSquared;
    private transient double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double measure;
    private transient double lowerMeasure;
    private transient double lowerNc;
    private transient double[][] lowerPdf;
    private transient double upperMeasure = 0.0;
    private transient double upperNc = 0.0;
    private transient double[][] upperPdf;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a CohenDRTest.
     */
    public CohenDR() {
        super();
        reset();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return the value for d.
     *
     * @param rValue correlation coefficient
     * @return d value
     */
    private static double calcD(final double rValue) {
        double result;
        if (Math.abs(rValue) < 1) {
            final double rSquared = rValue * rValue;
            result = Math.sqrt((4 * rSquared) / (1 - rSquared));
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Return the value for t.
     *
     * @param rValue correlation coefficient
     * @param size sample size
     * @return t value
     */
    private static double calcT(final double rValue, final double size) {
        double result;
        if (Math.abs(rValue) < 1 && size > 2) {
            final double rSquared = rValue * rValue;
            result = rValue / Math.sqrt((1 - rSquared) / (size - 2));
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        setErrorMessage("");
        testStatistic = calcT(rValue, size);
        measure = calcD(rValue);
        // Lower, upper non-centrality parameters
        final double alpha = 1 - confidence;
        try {
            lowerNc = ConfIntNct.findNonCentrality(testStatistic, df,
                    1 - (alpha * 0.5));
            upperNc = ConfIntNct.findNonCentrality(testStatistic, df, alpha * 0.5);
        } catch (ArithmeticException ex) {
            setErrorMessage(ex.getLocalizedMessage());
            doStateChanged();
            return;
        }
        // Lower, upper Cohen's d
        lowerMeasure = lowerNc / Math.sqrt(size);
        upperMeasure = upperNc / Math.sqrt(size);
        // PDF curves
        lowerPdf = ConfIntNct.createPdf(testStatistic, df, lowerNc, lowerNc, upperNc);
        upperPdf = ConfIntNct.createPdf(testStatistic, df, upperNc, lowerNc, upperNc);
        doStateChanged();
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public String getMeasureName() {
        return "Cohen's d";
    }

    @Override
    public String getMeasureSymbol() {
        return "d";
    }

    @Override
    public String getTestName() {
        return "R";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "t";
    }

    @Override
    public void reset() {
        setSize(0);
        setRValue(0);
        setConfidence(0.95);
    }

    // *************************************************************************
    // INPUT GETTER/SETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Correlation coefficient (r)")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Correlation coefficient.")
    public double getRValue() {
        return rValue;
    }

    public void setRValue(final double value) {
        rValue = Math.min(Constants.MAX_R, Math.max(Constants.MIN_R, value));
        rSquared = rValue * rValue;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Size (n)")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Size of the sample. If this value is "
            + "entered, the degrees of freedom will be automatically derived.")
    public double getSize() {
        return size;
    }

    public void setSize(final double value) {
        size = Math.max(Constants.MIN_SS, value);
        df = calcDF(size);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Deg Free (df)")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Degrees of freedom of the sample. If "
            + "this value is entered, the sample size will be automatically "
            + "derived.")
    public double getDf() {
        return df;
    }

    public void setDf(final double value) {
        df = Math.max(Constants.MIN_DF, value);
        size = calcSize(df);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 4)
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
    @NameAnnotation(value = "R Squared")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "R squared.")
    public double getRSquared() {
        return rSquared;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "t Statistic")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "t statistic. If this value is entered, "
            + "the sample mean, population mean, standard deviation, and "
            + "standard error will clear to zero and Cohen's d will be "
            + "calculated from the t statistic.")
    @Override
    public double getTestStatistic() {
        return testStatistic;
    }

    // *************************************************************************
    // RESULT GETTERS
    // *************************************************************************
    @Override
    public double getAlpha() {
        return 1 - confidence;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower d")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "Minimum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Cohen's d")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "Cohen's d.")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper d")
    @IndexAnnotation(value = 9)
    @DescriptionAnnotation(value = "Maximum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.DELTA_LOWER + "min)")
    @IndexAnnotation(value = 10)
    @DescriptionAnnotation(value = "Non-centrality parameter ("
            + Constants.DELTA_LOWER + ") corresponding to the minimum value of "
            + "Cohen's d on the confidence interval.")
    @Override
    public double getLowerNc() {
        return lowerNc;
    }

    @Override
    public double[][] getLowerPdf() {
        return lowerPdf;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper NC (" + Constants.DELTA_LOWER + "max)")
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "Non-centrality parameter ("
            + Constants.DELTA_LOWER + ") corresponding to the maximum value of "
            + "Cohen's d on the confidence interval.")
    @Override
    public double getUpperNc() {
        return upperNc;
    }

    @Override
    public double[][] getUpperPdf() {
        return upperPdf;
    }
}