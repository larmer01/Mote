package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Cohen's d, dependent t (sd diff) test.
 */
public final class CohenDDepTDiff extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double df;
    private double mean1;
    private double mean2;
    private double meanDiff;
    private double size;
    private double stdDevDiff;
    private double stdErrDiff;
    private double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double measure;
    /**
     * True to fix stdDevDiff and make stdErrDiff variable; false otherwise
     */
    private transient boolean fixedSD;
    private transient double lowerMeasure;
    private transient double lowerNc;
    private transient double[][] lowerPdf;
    private transient double upperMeasure = 0.0;
    private transient double upperNc = 0.0;
    private transient double[][] upperPdf;
    /**
     * True if d should be calculated from inputs; false if from t
     */
    private transient boolean useInputs;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a DependentTSDDiffTest.
     */
    public CohenDDepTDiff() {
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
        if (fixedSD) {
            stdErrDiff = calcStdErr(size, stdDevDiff);
        } else {
            stdDevDiff = calcStdDev(size, stdErrDiff);
        }
    }

    /**
     * Return the value for d.
     *
     * @param meanDiff mean difference
     * @param stdDevDiff standard deviation difference
     * @return d value
     */
    private static double calcD(final double meanDiff,
            final double stdDevDiff) {
        double result;
        if (stdDevDiff != 0) {
            result = meanDiff / stdDevDiff;
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * Return the value for t.
     *
     * @param meanDiff mean difference
     * @param stdErrDiff standard error difference
     * @return t value
     */
    private static double calcT(final double meanDiff,
            final double stdErrDiff) {
        double result;
        if (stdErrDiff != 0) {
            result = meanDiff / stdErrDiff;
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        if (useInputs) {
            testStatistic = calcT(meanDiff, stdErrDiff);
            measure = calcD(meanDiff, stdDevDiff);
        } else {
            measure = testStatistic / Math.sqrt(size);
        }
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
        return "Dependent t (SD Diff) Test";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "t";
    }

    @Override
    public void reset() {
        fixedSD = true;
        useInputs = true;
        setSize(0);
        setMean1(0);
        setMean2(0);
        setStdDevDiff(0);
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
        useInputs = true;
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
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Mean Difference (Mdiff)")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "The difference between sample mean 1 "
            + "and sample mean 2.")
    public double getMeanDiff() {
        return meanDiff;
    }

    public void setMeanDiff(final double value) {
        meanDiff = value;
        mean1 = 0;
        mean2 = 0;
        stdDevDiff = 0;
        stdErrDiff = 0;
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Std Dev Difference (" + Constants.SIGMA_LOWER +
            "diff)")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Standard deviation of the difference "
            + "scores. If this value is entered, the standard error will be "
            + "automatically derived and any changes to the sample size will "
            + "only affect the standard error.")
    public double getStdDevDiff() {
        return stdDevDiff;
    }

    public void setStdDevDiff(final double value) {
        fixedSD = true;
        stdDevDiff = value;
        stdErrDiff = calcStdErr(size, stdDevDiff);
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Std Err Difference (SEdiff)")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "Standard error of the difference "
            + "scores. If this value is entered, the standard deviation will "
            + "be automatically derived and any changes to the sample size "
            + "will only affect the standard deviation.")
    public double getStdErrDiff() {
        return stdErrDiff;
    }

    public void setStdErrDiff(final double value) {
        fixedSD = false;
        stdErrDiff = value;
        stdDevDiff = calcStdDev(size, stdErrDiff);
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Size (n)")
    @IndexAnnotation(value = 6)
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
    @IndexAnnotation(value = 7)
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
    @NameAnnotation(value = "t Statistic")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "t statistic. If this value is entered, the "
            + "sample means, mean difference, standard deviation difference, "
            + "and standard error difference will clear to zero and Cohen's d "
            + "will be calculated from the t statistic.")
    @Override
    public double getTestStatistic() {
        return testStatistic;
    }

    public void setTestStatistic(final double value) {
        testStatistic = value;
        mean1 = 0;
        mean2 = 0;
        meanDiff = 0;
        stdDevDiff = 0;
        stdErrDiff = 0;
        useInputs = false;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 9)
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
    // RESULT GETTERS
    // *************************************************************************
    @Override
    public double getAlpha() {
        return 1 - confidence;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower d")
    @IndexAnnotation(value = 10)
    @DescriptionAnnotation(value = "Minimum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Cohen's d")
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "Cohen's d.")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper d")
    @IndexAnnotation(value = 12)
    @DescriptionAnnotation(value = "Maximum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.DELTA_LOWER + "min)")
    @IndexAnnotation(value = 13)
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
    @IndexAnnotation(value = 14)
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