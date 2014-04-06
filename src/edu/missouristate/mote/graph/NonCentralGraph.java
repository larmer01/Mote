package edu.missouristate.mote.graph;

import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.TextAnchor;
import edu.missouristate.mote.effectsizes.AbstractNonCentralTest;

/**
 * Graphing support for tests based on a non-central distribution.
 */
public class NonCentralGraph extends AbstractGraph {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Statistical test represented by this graph. */
    private final transient AbstractNonCentralTest currentTest;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a NonCentralGraph.
     *
     * @param test statistical test
     */
    public NonCentralGraph(final AbstractNonCentralTest test) {
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
        dataset.addSeries(0, currentTest.getLowerPdf());
        dataset.addSeries(1, currentTest.getUpperPdf());
        setDataset(dataset);
        // Confidence interval x/y values
        final double xLeft = currentTest.getLowerNc();
        final double xMiddle = currentTest.getTestStatistic();
        final double xRight = currentTest.getUpperNc();
        final double yTop = Math.max(findMaxYValue(currentTest.getLowerPdf()),
                findMaxYValue(currentTest.getUpperPdf()));
        final double yBar = yTop * 0.8;
        final double yMetric = yTop * 0.2;
        // Confidence interval vertical line annotations
        removeAnnotations();
        addAnnotation(new XYLineAnnotation(xLeft, 0.0, xLeft, yTop));
        addAnnotation(new XYLineAnnotation(xMiddle, 0.0, xMiddle, yMetric));
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
        // Actual metric text annotation
        final String actMetric = String.format(currentTest.getMeasureSymbol()
                + "=%.4f", currentTest.getMeasure());
        final XYTextAnnotation actMetricAnn = new XYTextAnnotation(actMetric,
                xMiddle, yMetric);
        actMetricAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(actMetricAnn);
        // Right metric text annotation
        final String rightMetric = String.format(currentTest.getMeasureSymbol()
                + "=%.4f", currentTest.getUpperMeasure());
        final XYTextAnnotation rightMetricAnn = new XYTextAnnotation(
                rightMetric, xRight, yTop);
        rightMetricAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(rightMetricAnn);
        // CI text annotation
        final int confidence = (int) (currentTest.getConfidence() * 100);
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
     * Refresh this NonCentralGraph.
     */
    @Override
    public final void refresh() {
        setTitle(currentTest);
        if (currentTest.getLowerPdf() != null) {
            updateChart();
        }
    }
}
