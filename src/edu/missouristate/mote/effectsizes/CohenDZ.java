package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Cohen's d, Z test.
 */
public final class CohenDZ extends AbstractNormalTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double populationMean;
    private double sampleDf;
    private double sampleMean;
    private double sampleSize;
    private double populationStdDev;
    private double populationStdErr;
    private double testStatistic;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double deviations;
    private transient double measure;
    /**
     * True to fix sampleStdDev and make sampleStdErr variable; false otherwise
     */
    private transient boolean fixedSD;
    private transient double lowerMeasure;
    private transient double[][] pdf;
    private transient double upperMeasure;
    /**
     * True if d should be calculated from inputs; false if from Z
     */
    private transient boolean useInputs;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a CohenDZTest.
     */
    public CohenDZ() {
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
            populationStdErr = calcStdErr(sampleSize, populationStdDev);
        } else {
            populationStdDev = calcStdDev(sampleSize, populationStdErr);
        }
    }

    /**
     * Return the value for d.
     *
     * @param sampleMean sample mean
     * @param populationMean population mean
     * @param populationStdDev population standard deviation
     * @return d value
     */
    private static double calcD(final double sampleMean,
            final double populationMean, final double populationStdDev) {
        double result;
        if (populationStdDev != 0) {
            result = (sampleMean - populationMean) / populationStdDev;
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * Return the value for z.
     *
     * @param sampleMean sample mean
     * @param populationMean population mean
     * @param populationStdErr population standard error
     * @return z value
     */
    private static double calcZ(final double sampleMean,
            final double populationMean, final double populationStdErr) {
        double result;
        if (populationStdErr != 0) {
            result = (sampleMean - populationMean) / populationStdErr;
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
            testStatistic = calcZ(sampleMean, populationMean, populationStdErr);
            measure = calcD(sampleMean, populationMean, populationStdDev);
        } else {
            measure = testStatistic / Math.sqrt(sampleSize);
        }
        // Standard deviations
        final double alpha = 1 - confidence;
        deviations = ConfIntNormal.findX(1 - (alpha * 0.5));
        // Lower, upper Cohen's d
        lowerMeasure = measure - deviations * populationStdDev;
        upperMeasure = measure + deviations * populationStdDev;
        // PDF
        pdf = ConfIntNormal.createPdf();
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
        return "Z Test";
    }

    @Override
    public String getTestStatisticSymbol() {
        return "Z";
    }

    @Override
    public void reset() {
        fixedSD = true;
        useInputs = true;
        setSampleSize(0);
        setSampleMean(0);
        setPopulationMean(0);
        setPopulationStdDev(0);
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
    @NameAnnotation(value = "Population Std Dev (" + Constants.SIGMA_LOWER + ")")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Standard deviation of the population. If "
            + "this value is entered, the standard error will be "
            + "automatically derived and any changes to the size will "
            + "only affect the standard error.")
    public double getPopulationStdDev() {
        return populationStdDev;
    }

    public void setPopulationStdDev(final double value) {
        fixedSD = true;
        populationStdDev = value;
        populationStdErr = calcStdErr(sampleSize, populationStdDev);
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Est Population Std Err (SE)")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Estimated standard error of the population. If "
            + "this value is entered, the standard deviation will be "
            + "automatically derived and any changes to the size will "
            + "only affect the standard deviation.")
    public double getPopulationStdErr() {
        return populationStdErr;
    }

    public void setPopulationStdErr(final double value) {
        fixedSD = false;
        populationStdErr = value;
        populationStdDev = calcStdDev(sampleSize, populationStdErr);
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
    @NameAnnotation(value = "Z Statistic")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "Z statistic. If this value is entered, "
            + "the sample mean and population means will clear to zero and "
            + "Cohen's d will be calculated from the z statistic.")
    @Override
    public double getTestStatistic() {
        return testStatistic;
    }

    public void setTestStatistic(final double value) {
        testStatistic = value;
        sampleMean = 0;
        populationMean = 0;
        useInputs = false;
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
    @NameAnnotation(value = "Deviations")
    @IndexAnnotation(value = 12)
    @DescriptionAnnotation(value = "Number of standard deviations on either "
            + "side of d making up the confidence interval.")
    @Override
    public double getDeviations() {
        return deviations;
    }

    @Override
    public double[][] getPdf() {
        return pdf;
    }
}