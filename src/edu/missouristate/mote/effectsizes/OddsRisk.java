package edu.missouristate.mote.effectsizes;

import edu.missouristate.mote.Constants;
import edu.missouristate.mote.propertygrid.CategoryAnnotation;
import edu.missouristate.mote.propertygrid.DescriptionAnnotation;
import edu.missouristate.mote.propertygrid.IndexAnnotation;
import edu.missouristate.mote.propertygrid.NameAnnotation;

/**
 * Odds ratio and relative risk.
 */
public final class OddsRisk extends AbstractNormalTest {

    // *************************************************************************
    // INPUT FIELDS
    // *************************************************************************
    private double confidence;
    private double value11;
    private double value12;
    private double value21;
    private double value22;
    // *************************************************************************
    // RESULT FIELDS
    // *************************************************************************
    private transient double deviations;
    private transient double measure;
    private transient double lowerMeasure;
    private transient double[][] pdf;
    private transient double upperMeasure;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a OddsRisk.
     */
    public OddsRisk() {
        super();
        reset();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return the value for omega.
     *
     * @param sampleMean sample mean
     * @param populationMean population mean
     * @param populationStdDev population standard deviation
     * @return d value
     */
    private static double calcOmega(final double value11, final double value12,
            final double value21, final double value22) {
        if (value12 == 0 || value21 == 0 || value22 == 0) {
            return 0;
        } else {
            return (value11 / value12) / (value21 / value22);
        }
    }

    /**
     * Calculate the effect size.
     */
    private void calculate() {
        measure = calcOmega(value11, value12, value21, value22);
        // Standard deviations
        final double alpha = 1 - confidence;
        deviations = ConfIntNormal.findX(1 - (alpha * 0.5));
        // Lower, upper measure
        final double se = Math.sqrt(1 / value11 + 1 / value12 + 1 / value21
                + 1 / value22);
        lowerMeasure = Math.exp(Math.log(measure) - deviations * se);
        upperMeasure = Math.exp(Math.log(measure) + deviations * se);
        // PDF
        pdf = ConfIntNormal.createPdf();
        doStateChanged();
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public String getMeasureName() {
        return "Odd's Ratio";
    }

    @Override
    public String getMeasureSymbol() {
        return "OR";
    }

    @Override
    public String getTestName() {
        return Constants.OMEGA_UPPER;
    }

    @Override
    public String getTestStatisticSymbol() {
        return "X";
    }

    @Override
    public void reset() {
        setValue11(0);
        setValue12(0);
        setValue21(0);
        setValue22(0);
        setConfidence(0.95);
    }

    // *************************************************************************
    // INPUT GETTER/SETTERS
    // *************************************************************************
    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Value[1][1]")
    @IndexAnnotation(value = 1)
    @DescriptionAnnotation(value = "Row 1, column 1.")
    public double getValue11() {
        return value11;
    }

    public void setValue11(final double value) {
        value11 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Value[1][2]")
    @IndexAnnotation(value = 2)
    @DescriptionAnnotation(value = "Row 1, column 2.")
    public double getValue12() {
        return value12;
    }

    public void setValue12(final double value) {
        value12 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Value[2][1]")
    @IndexAnnotation(value = 3)
    @DescriptionAnnotation(value = "Row 2, column 1.")
    public double getValue21() {
        return value21;
    }

    public void setValue21(final double value) {
        value21 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Value[2][2]")
    @IndexAnnotation(value = 4)
    @DescriptionAnnotation(value = "Row 2, column 2.")
    public double getValue22() {
        return value22;
    }

    public void setValue22(final double value) {
        value22 = value;
        calculate();
    }

    @CategoryAnnotation(value = Constants.INPUT_CATEGORY)
    @NameAnnotation(value = "Confidence (1 - " + Constants.ALPHA_LOWER + ")")
    @IndexAnnotation(value = 5)
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
    @NameAnnotation(value = "Lower OR")
    @IndexAnnotation(value = 6)
    @DescriptionAnnotation(value = "Minimum value of the odds ratio on the "
            + "confidence interval.")
    @Override
    public double getLowerMeasure() {
        return lowerMeasure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "OR")
    @IndexAnnotation(value = 7)
    @DescriptionAnnotation(value = "Odds ratio.")
    @Override
    public double getMeasure() {
        return measure;
    }

    @CategoryAnnotation(value = Constants.OUTPUT_CATEGORY)
    @NameAnnotation(value = "Upper OR")
    @IndexAnnotation(value = 8)
    @DescriptionAnnotation(value = "Maximum value of the odds ratio on the "
            + "confidence interval.")
    @Override
    public double getUpperMeasure() {
        return upperMeasure;
    }

    @Override
    public double getDeviations() {
        return deviations;
    }
    
    @Override
    public double getTestStatistic() {
        return 0;
    }

    @Override
    public double[][] getPdf() {
        return pdf;
    }
}