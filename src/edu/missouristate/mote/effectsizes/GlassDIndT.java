package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Glass's delta, independent t test.
 */
public final class GlassDIndT extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double mean1;
    private double mean2;
    private double size1;
    private double size2;
    private double stdDev1;
    private double stdDev2;
    private double stdErr1;
    private double stdErr2;
    // *************************************************************************
    // DERIVED FIELDS
    // *************************************************************************
    private transient double df;
    private transient double size;
    private transient double stdDevPooled;
    private transient double stdErrPooled;
    private transient double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    /**
     * True to fix stdDev1 and make stdErr1 variable; false otherwise
     */
    private transient boolean fixedSD1;
    /**
     * True to fix stdDev2 and make stdErr2 variable; false otherwise
     */
    private transient boolean fixedSD2;
    private transient double measure;
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
     * Initialize a new instance of a GlassDIndTTest.
     */
    public GlassDIndT() {
        super();
        reset();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Adjust either the standard deviation or the standard error depending on
     * which one was directly entered and which was derived. This will also
     * update the pooled standard deviation/error.
     */
    private void adjustSdSe() {
        if (fixedSD1) {
            stdErr1 = calcStdErr(size1, stdDev1);
        } else {
            stdDev1 = calcStdDev(size1, stdErr1);
        }
        if (fixedSD2) {
            stdErr2 = calcStdErr(size2, stdDev2);
        } else {
            stdDev2 = calcStdDev(size2, stdErr2);
        }
        stdDevPooled = calcSDPooled(stdDev1, stdDev2, size1, size2);
        stdErrPooled = calcSEPooled(stdDevPooled, size1, size2);
    }

    /**
     * Return the value for d.
     *
     * @param mean1 group 1 mean
     * @param mean2 group 2 mean
     * @param stdDev2 control group standard deviation
     *
     * @return d value
     */
    private static double calcD(final double mean1, final double mean2,
            final double stdDev2) {
        double result;
        if (stdDev2 != 0) {
            result = (mean1 - mean2) / stdDev2;
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Return the pooled standard deviation.
     *
     * @param stdDev1 group 1 standard deviation
     * @param stdDev2 group 2 standard deviation
     * @param size1 group 1 size
     * @param size2 group 2 size
     * @return pooled standard deviation
     */
    private static double calcSDPooled(final double stdDev1,
            final double stdDev2, final double size1, final double size2) {
        double result;
        if (size1 + size2 > 2) {
            result = Math.sqrt(((size1 - 1) * stdDev1 * stdDev1
                    + (size2 - 1) * stdDev2 * stdDev2) / (size1 + size2 - 2));
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Return the pooled standard error.
     *
     * @param stdDevPooled pooled standard deviation
     * @param size1 group 1 size
     * @param size2 group 2 size
     * @return
     */
    private static double calcSEPooled(final double stdDevPooled,
            final double size1, final double size2) {
        double result;
        if (size1 > 0 && size2 > 0) {
            result = Math.sqrt((stdDevPooled * stdDevPooled / size1)
                    + (stdDevPooled * stdDevPooled) / size2);
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Return the value for t.
     *
     * @param mean1 group 1 mean
     * @param mean2 group 2 mean
     * @param stdErrPooled pooled standard error
     *
     * @return t value
     */
    private static double calcT(final double mean1, final double mean2,
            final double stdErrPooled) {
        double result;
        if (stdErrPooled != 0) {
            result = (mean1 - mean2) / stdErrPooled;
        } else {
            result = 0.0;
        }
        return result;
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        if (df <= 0) {
            setErrorMessage("degrees of freedom <= 0");
            return;
        }
        setErrorMessage("");
        testStatistic = calcT(mean1, mean2, stdErrPooled);
        measure = calcD(mean1, mean2, stdDev2);
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
        final double bottom = Math.sqrt((size1 * size2) / size);
        lowerMeasure = lowerNc / bottom;
        upperMeasure = upperNc / bottom;
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
        return "Glass's " + Constants.DELTA_UPPER;
    }

    @Override
    public String getMeasureSymbol() {
        return Constants.DELTA_UPPER;
    }

    @Override
    public String getTestName() {
        return "Independent t Test";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "t";
    }

    @Override
    public void reset() {
        fixedSD1 = true;
        fixedSD2 = true;
        setSize1(0);
        setSize2(0);
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
    @NameAnnotation(value = "Group 1 Mean (m1)")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Mean of the sample data from group 1.")
    public double getMean1() {
        return mean1;
    }

    public void setMean1(final double value) {
        mean1 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 2 Mean (m2)")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Mean of the sample data from group 2, the "
            + "control group.")
    public double getMean2() {
        return mean2;
    }

    public void setMean2(final double value) {
        mean2 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 1 Std Dev (" + Constants.SIGMA_LOWER + "1)")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Standard deviation of the samples from "
            + "group 1. If this value is entered, the standard error for "
            + "group 1 will be automatically derived and any changes to the "
            + "sample size will only affect the standard error.")
    public double getStdDev1() {
        return stdDev1;
    }

    public void setStdDev1(final double value) {
        fixedSD1 = true;
        stdDev1 = value;
        stdErr1 = calcStdErr(size1, stdDev1);
        stdDevPooled = calcSDPooled(stdDev1, stdDev2, size1, size2);
        stdErrPooled = calcSEPooled(stdDevPooled, size1, size2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 2 Std Dev (" + Constants.SIGMA_LOWER + "1)")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Standard deviation of the samples from "
            + "group 2, the control group. If this value is entered, the "
            + "standard error for group 2 will be automatically derived and "
            + "any changes to the sample size will only affect the standard "
            + "error.")
    public double getStdDev2() {
        return stdDev2;
    }

    public void setStdDev2(final double value) {
        fixedSD2 = true;
        stdDev2 = value;
        stdErr2 = calcStdErr(size2, stdDev2);
        stdDevPooled = calcSDPooled(stdDev1, stdDev2, size1, size2);
        stdErrPooled = calcSEPooled(stdDevPooled, size1, size2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 1 Std Err (SE1)")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "Standard error of the samples from "
            + "group 1. If this value is entered, the standard deviation for "
            + "group 1 will be automatically derived and any changes to the "
            + "sample size will only affect the standard deviation.")
    public double getStdErr1() {
        return stdErr1;
    }

    public void setStdErr1(final double value) {
        fixedSD1 = false;
        stdErr1 = value;
        stdDev1 = calcStdDev(size1, stdErr1);
        stdDevPooled = calcSDPooled(stdDev1, stdDev2, size1, size2);
        stdErrPooled = calcSEPooled(stdDevPooled, size1, size2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 2 Std Err (SE2)")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "Standard error of the samples from "
            + "group 2, the control group. If this value is entered, the "
            + "standard deviation for group 2 will be automatically derived "
            + "and any changes to the sample size will only affect the "
            + "standard deviation.")
    public double getStdErr2() {
        return stdErr2;
    }

    public void setStdErr2(final double value) {
        fixedSD2 = false;
        stdErr2 = value;
        stdDev2 = calcStdDev(size2, stdErr2);
        stdDevPooled = calcSDPooled(stdDev1, stdDev2, size1, size2);
        stdErrPooled = calcSEPooled(stdDevPooled, size1, size2);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 1 Size (n1)")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "Size of the sample in group 1.")
    public double getSize1() {
        return size1;
    }

    public void setSize1(final double value) {
        size1 = Math.max(Constants.MIN_SS, value);
        size = size1 + size2;
        df = size - 2;
        adjustSdSe();
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Group 2 Size (n2)")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "Size of the sample in group 2.")
    public double getSize2() {
        return size2;
    }

    public void setSize2(final double value) {
        size2 = Math.max(Constants.MIN_SS, value);
        size = size1 + size2;
        df = size - 2;
        adjustSdSe();
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
    // DERIVED GETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Size (n)")
    @IndexAnnotation(value = 10)
    @DescriptionAnnotation(value = "Total size of the samples from groups 1 "
            + "and 2. Automatically calculated from n1 and n2.")
    public double getSize() {
        return size;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Degrees Freedom (df)")
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "Total degrees of freedom of from groups "
            + "1 and 2. Automatically calculated from n.")
    public double getDf() {
        return df;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Pooled Std Dev (" + Constants.SIGMA_LOWER + "pooled)")
    @IndexAnnotation(value = 12)
    @DescriptionAnnotation(value = "Pooled standard deviation of the samples "
            + "from groups 1 and 2. Automatically calculated from the sample "
            + "sizes and standard deviations.")
    public double getStdDevPooled() {
        return stdDevPooled;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "Pooled Std Err (SEpooled)")
    @IndexAnnotation(value = 13)
    @DescriptionAnnotation(value = "Pooled standard error of the samples "
            + "from groups 1 and 2. Automatically calculated from the sample "
            + "sizes and standard deviations.")
    public double getStdErrPooled() {
        return stdErrPooled;
    }

    @CategoryAnnotation(value = Constants.DERIVED_CATEGORY)
    @NameAnnotation(value = "t Statistic")
    @IndexAnnotation(value = 14)
    @DescriptionAnnotation(value = "t statistic.")
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
    @NameAnnotation(value = "Lower " + Constants.DELTA_UPPER)
    @IndexAnnotation(value = 15)
    @DescriptionAnnotation(value = "Minimum value of Glass's "
            + Constants.DELTA_UPPER + " on the confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Glass's " + Constants.DELTA_UPPER)
    @IndexAnnotation(value = 16)
    @DescriptionAnnotation(value = "Glass's " + Constants.DELTA_UPPER + ".")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper " + Constants.DELTA_UPPER)
    @IndexAnnotation(value = 17)
    @DescriptionAnnotation(value = "Maximum value of Glass's "
            + Constants.DELTA_UPPER + " on the confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.DELTA_LOWER + "min)")
    @IndexAnnotation(value = 18)
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
    @IndexAnnotation(value = 19)
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