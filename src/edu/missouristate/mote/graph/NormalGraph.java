package edu.missouristate.mote.graph;

import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.TextAnchor;
import edu.missouristate.mote.effectsizes.AbstractNormalTest;

/**
 * Graphing support for tests based on the normal distribution.
 */
public class NormalGraph extends AbstractGraph {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************

    /** Alpha value that will cause us to re-anchor the label text to fit. */
    private static final double MIN_ALPHA = 0.03;

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Statistical test represented by this graph. */
    private final transient AbstractNormalTest currentTest;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a GraphPanel.
     *
     * @param test statistical test
     */
    public NormalGraph(final AbstractNormalTest test) {
        super(test);
        currentTest = test;
        setXAxisLabel(test.getTestStatisticSymbol());
        setYAxisLabel("density");
        refresh();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Update the chart based on the current statistical test.
     */
    private void updateChart() {
        // Lower and upper PDF curves
        final DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries(0, currentTest.getPdf());
        setDataset(dataset);
        // Confidence interval x/y values
        final double xLeft = -currentTest.getDeviations();
        final double xRight = currentTest.getDeviations();
        final double pdfHeight = findMaxYValue(currentTest.getPdf());
        final double pdfCurrent = findApproxYValue(xLeft, currentTest.getPdf());
        final double yTop = Math.max(pdfCurrent, pdfHeight * 0.5);
        final double yBar = yTop * 0.8;
        // Confidence interval vertical line annotations
        removeAnnotations();
        addAnnotation(new XYLineAnnotation(xLeft, 0.0, xLeft, yTop));
        addAnnotation(new XYLineAnnotation(xRight, 0.0, xRight, yTop));
        // Confidence interval horizontal line annotation
        addAnnotation(new XYLineAnnotation(xLeft, yBar, xRight, yBar));
        final XYPointerAnnotation leftArrow = new XYPointerAnnotation("", xLeft,
                yBar, 0.0);
        leftArrow.setTipRadius(0.0);
        addAnnotation(leftArrow);
        final XYPointerAnnotation rightArrow = new XYPointerAnnotation("",
                xRight, yBar, Math.PI);
        rightArrow.setTipRadius(0.0);
        addAnnotation(rightArrow);
        // Left metric text annotation
        final String leftMetric = String.format(currentTest.getMeasureSymbol()
                + "=%.4f", currentTest.getLowerMeasure());
        final XYTextAnnotation leftMetricAnn = new XYTextAnnotation(leftMetric,
                xLeft, yTop);
        leftMetricAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(leftMetricAnn);
        // Right metric text annotation
        final String rightMetric = String.format(currentTest.getMeasureSymbol()
                + "=%.4f", currentTest.getUpperMeasure());
        final XYTextAnnotation rightMetricAnn = new XYTextAnnotation(
                rightMetric, xRight, yTop);
        if (currentTest.getAlpha() < MIN_ALPHA) {
            rightMetricAnn.setTextAnchor(TextAnchor.BOTTOM_RIGHT);
        } else {
            rightMetricAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        }
        addAnnotation(rightMetricAnn);
        // CI text annotation
        final int confidence = (int) ((1 - currentTest.getAlpha()) * 100);
        final String confStr = confidence + "% confidence";
        final XYTextAnnotation confAnn = new XYTextAnnotation(confStr,
                (xLeft + xRight) * 0.5, yBar);
        confAnn.setTextAnchor(TextAnchor.BOTTOM_CENTER);
        addAnnotation(confAnn);
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Refresh this NormalGraph.
     */
    @Override
    public final void refresh() {
        setTitle(currentTest);
        if (currentTest.getPdf() != null) {
            updateChart();
        }
    }
}
