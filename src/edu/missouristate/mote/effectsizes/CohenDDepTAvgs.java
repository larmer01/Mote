package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Cohen's d, dependent t (averages) test.
 */
public final class CohenDDepTAvgs extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double df;
    private double mean1;
    private double mean2;
    private double meanDiff;
    private double size;
    private double stdDev1;
    private double stdDev2;
    private double stdErr1;
    private double stdErr2;
    // *************************************************************************
    // DERIVED FIELDS
    // *************************************************************************
    private transient double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double measure;
    /**
     * True to fix stdDev1 and make stdErr1 variable; false otherwise
     */
    private transient boolean fixedSD1;
    /**
     * True to fix stdDev2 and make stdErr2 variable; false otherwise
     */
    private transient boolean fixedSD2;
    private transient double lowerMeasure;
    private transient double lowerNc;
    private transient double[][] lowerPdf;
    private transient double upperMeasure;
    private transient double upperNc;
    private transient double[][] upperPdf;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a CohenDDepTAvgsTest.
     */
    public CohenDDepTAvgs() {
        super();
        reset();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Adjust either the standard deviation or the standard error depending on
     * which one was directly entered and which was derived.
     */
    private void adjustSdSe() {
        if (fixedSD1) {
            stdErr1 = calcStdErr(size, stdDev1);
        } else {
            stdDev1 = calcStdDev(size, stdErr1);
        }
        if (fixedSD2) {
            stdErr2 = calcStdErr(size, stdDev2);
        } else {
            stdDev2 = calcStdDev(size, stdErr2);
        }
    }

    /**
     * Return the value for d.
     *
     * @param meanDiff mean difference
     * @param stdDev1 time 1 standard deviation
     * @param stdDev2 time 2 standard deviation
     *
     * @return d value
     */
    private static double calcD(final double meanDiff, final double stdDev1,
            final double stdDev2) {
        double result = 0.0;
        final double meanSD = (stdDev1 + stdDev2) * 0.5;
        if (meanSD != 0) {
            result = meanDiff / meanSD;
        }
        return result;
    }

    /**
     * Return the value for t.
     *
     * @param meanDiff mean difference
     * @param stdDev1 time 1 standard error
     * @param stdDev2 time 2 standard error
     *
     * @return t value
     */
    private static double calcT(final double meanDiff, final double stdErr1,
            final double stdErr2) {
        double result = 0.0;
        final double meanSE = (stdErr1 + stdErr2) * 0.5;
        if (meanSE != 0) {
            result = meanDiff / meanSE;
        }
        return result;
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        setErrorMessage("");
        testStatistic = calcT(meanDiff, stdErr1, stdErr2);
        measure = calcD(meanDiff, stdDev1, stdDev2);
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
        return "Dependent t (Averages) Test";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "t";
    }

    @Override
    public void reset() {
        fixedSD1 = true;
        fixedSD2 = true;
        setSize(0);
        setMean1(0);
        setMean2(0);
        setStdDev1(0);
        setStdDev2(0);
        setConfidence(0.95);
    }

    // *************************************************************************
    // INPUT GETTER/SETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 1 Mean (m1)")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Mean of the sample data from time 1.")
    public double getMean1() {
        return mean1;
    }

    public void setMean1(final double value) {
        mean1 = value;
        meanDiff = mean1 - mean2;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 2 Mean (m2)")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Mean of the sample data from time 2.")
    public double getMean2() {
        return mean2;
    }

    public void setMean2(final double value) {
        mean2 = value;
        meanDiff = mean1 - mean2;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Mean Difference (Mdiff)")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "The difference between sample mean 1 and "
            + "sample mean 2.")
    public double getMeanDiff() {
        return meanDiff;
    }

    public void setMeanDiff(final double value) {
        meanDiff = value;
        mean1 = 0;
        mean2 = 0;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 1 Std Dev (" + Constants.SIGMA_LOWER + "1)")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Standard deviation of the sample from time "
            + "1. If this value is entered, the standard error for time 1 will "
            + "be automatically derived and any changes to the sample size "
            + "will only affect the standard error.")
    public double getStdDev1() {
        return stdDev1;
    }

    public void setStdDev1(final double value) {
        fixedSD1 = true;
        stdDev1 = value;
        stdErr1 = calcStdErr(size, stdDev1);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 2 Std Dev (" + Constants.SIGMA_LOWER + "1)")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "Standard deviation of the sample from time "
            + "2. If this value is entered, the standard error for time 2 will "
            + "be automatically derived and any changes to the sample size "
            + "will only affect the standard error.")
    public double getStdDev2() {
        return stdDev2;
    }

    public void setStdDev2(final double value) {
        fixedSD2 = true;
        stdDev2 = value;
        stdErr2 = calcStdErr(size, stdDev2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 1 Std Err (SE1)")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "Standard error of the sample from time 1. "
            + "If this value is entered, the standard deviation for time 1 "
            + "will be automatically derived and any changes to the sample "
            + "size will only affect the standard deviation.")
    public double getStdErr1() {
        return stdErr1;
    }

    public void setStdErr1(final double value) {
        fixedSD1 = false;
        stdErr1 = value;
        stdDev1 = calcStdDev(size, stdErr1);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Time 2 Std Err (SE2)")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "Standard error of the sample from time 2. "
            + "If this value is entered, the standard deviation for time 2 "
            + "will be automatically derived and any changes to the sample "
            + "size will only affect the standard deviation.")
    public double getStdErr2() {
        return stdErr2;
    }

    public void setStdErr2(final double value) {
        fixedSD2 = false;
        stdErr2 = value;
        stdDev2 = calcStdDev(size, stdErr2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Size (n)")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "Size of the sample. If this value is "
            + "entered, the degrees of freedom will be automatically derived.")
    public double getSize() {
        return size;
    }

    public void setSize(final double value) {
        size = Math.max(Constants.MIN_SS, value);
        df = calcDF(size);
        adjustSdSe();
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Degrees Freedom (df)")
    @IndexAnnotation(value = 9)
    @DescriptionAnnotation(value = "Degrees of freedom of the sample. If this "
            + "value is entered, the sample size will be automatically "
            + "derived.")
    public double getDf() {
        return df;
    }

    public void setDf(final double value) {
        df = Math.max(Constants.MIN_DF, value);
        size = calcSize(df);
        adjustSdSe();
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 10)
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
    @NameAnnotation(value = "t Statistic")
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "t statistic. This value is calculated by "
            + "using an average standard error for your repeated measurements "
            + "and should not be used for significance testing.")
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
    @IndexAnnotation(value = 12)
    @DescriptionAnnotation(value = "Minimum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Cohen's d")
    @IndexAnnotation(value = 13)
    @DescriptionAnnotation(value = "Cohen's d.")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper d")
    @IndexAnnotation(value = 14)
    @DescriptionAnnotation(value = "Maximum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.DELTA_LOWER + "min)")
    @IndexAnnotation(value = 15)
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
    @IndexAnnotation(value = 16)
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