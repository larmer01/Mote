package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Cohen's d, single sample t test.
 */
public final class CohenDSst extends AbstractNonCentralTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double populationMean;
    private double sampleDf;
    private double sampleMean;
    private double sampleSize;
    private double sampleStdDev;
    private double sampleStdErr;
    private double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double measure;
    /**
     * True to fix sampleStdDev and make sampleStdErr variable; false otherwise
     */
    private transient boolean fixedSD;
    private transient double lowerMeasure;
    private transient double lowerNc;
    private transient double[][] lowerPdf;
    private transient double upperMeasure;
    private transient double upperNc;
    private transient double[][] upperPdf;
    /**
     * True if d should be calculated from inputs; false if from t
     */
    private transient boolean useInputs;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a CohenDSstTest.
     */
    public CohenDSst() {
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
            sampleStdErr = calcStdErr(sampleSize, sampleStdDev);
        } else {
            sampleStdDev = calcStdDev(sampleSize, sampleStdErr);
        }
    }

    /**
     * Return the value for d.
     *
     * @param sampleMean sample mean
     * @param populationMean population mean
     * @param sampleStdDev sample standard deviation
     * @return d value
     */
    private static double calcD(final double sampleMean,
            final double populationMean, final double sampleStdDev) {
        double result = 0.0;
        if (sampleStdDev != 0) {
            result = (sampleMean - populationMean) / sampleStdDev;
        }
        return result;
    }

    /**
     * Return the value for t.
     *
     * @param sampleMean sample mean
     * @param populationMean population mean
     * @param sampleStdErr sample standard error
     * @return t value
     */
    private static double calcT(final double sampleMean,
            final double populationMean, final double sampleStdErr) {
        double result = 0.0;
        if (sampleStdErr != 0) {
            result = (sampleMean - populationMean) / sampleStdErr;
        }
        return result;
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        setErrorMessage("");
        if (useInputs) {
            testStatistic = calcT(sampleMean, populationMean, sampleStdErr);
            measure = calcD(sampleMean, populationMean, sampleStdDev);
        } else {
            measure = testStatistic / Math.sqrt(sampleSize);
        }
        // Lower, upper non-centrality parameters
        final double alpha = 1 - confidence;
        try {
            lowerNc = ConfIntNct.findNonCentrality(testStatistic, sampleDf,
                    1 - (alpha * 0.5));
            upperNc = ConfIntNct.findNonCentrality(testStatistic, sampleDf,
                    alpha * 0.5);
        } catch (ArithmeticException ex) {
            setErrorMessage(ex.getLocalizedMessage());
            doStateChanged();
            return;
        }
        // Lower, upper Cohen's d
        lowerMeasure = lowerNc / Math.sqrt(sampleSize);
        upperMeasure = upperNc / Math.sqrt(sampleSize);
        // PDF curves
        lowerPdf = ConfIntNct.createPdf(testStatistic, sampleDf, lowerNc, lowerNc,
                upperNc);
        upperPdf = ConfIntNct.createPdf(testStatistic, sampleDf, upperNc, lowerNc,
                upperNc);
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
        return "Single Sample t Test";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "t";
    }

    @Override
    public void reset() {
        fixedSD = true;
        useInputs = true;
        setSampleSize(0);
        setSampleMean(0);
        setPopulationMean(0);
        setSampleStdDev(0);
        setConfidence(0.95);
    }

    // *************************************************************************
    // INPUT GETTER/SETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Mean (M)")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Sample mean.")
    public double getSampleMean() {
        return sampleMean;
    }

    public void setSampleMean(final double value) {
        sampleMean = value;
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Population Mean (" + Constants.MU_LOWER + ")")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Population mean.")
    public double getPopulationMean() {
        return populationMean;
    }

    public void setPopulationMean(final double value) {
        populationMean = value;
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Std Dev (" + Constants.SIGMA_LOWER + ")")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Standard deviation of the sample. If "
            + "this value is entered, the standard error will be "
            + "automatically derived and any changes to the sample size will "
            + "only affect the standard error.")
    public double getSampleStdDev() {
        return sampleStdDev;
    }

    public void setSampleStdDev(final double value) {
        fixedSD = true;
        sampleStdDev = value;
        sampleStdErr = calcStdErr(sampleSize, sampleStdDev);
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Std Err (SE)")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Standard error of the sample. If "
            + "this value is entered, the standard deviation will be "
            + "automatically derived and any changes to the sample size will "
            + "only affect the standard deviation.")
    public double getSampleStdErr() {
        return sampleStdErr;
    }

    public void setSampleStdErr(final double value) {
        fixedSD = false;
        sampleStdErr = value;
        sampleStdDev = calcStdDev(sampleSize, sampleStdErr);
        useInputs = true;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Size (n)")
    @IndexAnnotation(value = 5)
    @DescriptionAnnotation(value = "Size of the sample. If this value is "
            + "entered, the degrees of freedom will be automatically derived.")
    public double getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(final double value) {
        sampleSize = Math.max(Constants.MIN_SS, value);
        sampleDf = calcDF(sampleSize);
        adjustSdSe();
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Sample Deg Free (df)")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "Degrees of freedom of the sample. If "
            + "this value is entered, the sample size will be automatically "
            + "derived.")
    public double getSampleDf() {
        return sampleDf;
    }

    public void setSampleDf(final double value) {
        sampleDf = Math.max(Constants.MIN_DF, value);
        sampleSize = calcSize(sampleDf);
        adjustSdSe();
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "t Statistic")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "t statistic. If this value is entered, "
            + "the sample mean, population mean, standard deviation, and "
            + "standard error will clear to zero and Cohen's d will be "
            + "calculated from the t statistic.")
    @Override
    public double getTestStatistic() {
        return testStatistic;
    }

    public void setTestStatistic(final double value) {
        testStatistic = value;
        useInputs = false;
        sampleMean = 0;
        populationMean = 0;
        sampleStdDev = 0;
        sampleStdErr = 0;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 8)
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
    @IndexAnnotation(value = 9)
    @DescriptionAnnotation(value = "Minimum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Cohen's d")
    @IndexAnnotation(value = 10)
    @DescriptionAnnotation(value = "Cohen's d.")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper d")
    @IndexAnnotation(value = 11)
    @DescriptionAnnotation(value = "Maximum value of Cohen's d on the "
            + "confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Lower NC (" + Constants.DELTA_LOWER + "min)")
    @IndexAnnotation(value = 12)
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
    @IndexAnnotation(value = 13)
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