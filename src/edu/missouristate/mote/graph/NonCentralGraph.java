package edu.missouristate.mote.graph;

import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.TextAnchor;
import edu.missouristate.mote.effectsizes.AbstractNonCentralTest;

/**
 *
 * @author tim
 */
public class NonCentralGraph extends AbstractGraph {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    private transient final AbstractNonCentralTest test;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a NonCentralGraph.
     */
    public NonCentralGraph(final AbstractNonCentralTest test) {
        super(test);
        this.test = test;
        setXAxisLabel(test.getTestStatisticSymbol());
        setYAxisLabel("density");
        refresh();
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    private void updateChart() {
        // Lower and upper PDF curves
        final DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries(0, test.getLowerPdf());
        dataset.addSeries(1, test.getUpperPdf());
        setDataset(dataset);
        // Confidence interval x/y values
        final double x1 = test.getLowerNc();
        //final double y1a = findApproxYValue(x1, test.getLowerPdf());
        final double x2 = test.getTestStatistic();
        //final double y2 = Math.max(findApproxYValue(x2, test.getLowerPdf()),
        //        findApproxYValue(x2, test.getUpperPdf()));
        final double y1 = Math.max(findMaxYValue(test.getLowerPdf()),
                findMaxYValue(test.getUpperPdf()));
        final double x3 = test.getUpperNc();
        //final double y3a = findApproxYValue(x3, test.getUpperPdf());
        //final double y1 = Math.max(y1a, 0.3 * Math.max(y1a, y3a));
        //final double y3 = Math.max(y3a, 0.3 * Math.max(y1a, y3a));
        final double y2 = y1 * 0.2;
        final double y3 = y1;
        final double yh = Math.min(y1, y3) * 0.8;
        // Confidence interval vertical line annotations
        removeAnnotations();
        addAnnotation(new XYLineAnnotation(x1, 0.0, x1, y1));
        addAnnotation(new XYLineAnnotation(x2, 0.0, x2, y2));
        addAnnotation(new XYLineAnnotation(x3, 0.0, x3, y3));
        // Confidence interval horizontal line annotation
        addAnnotation(new XYLineAnnotation(x1, yh, x3, yh));
        final XYPointerAnnotation leftArrow = new XYPointerAnnotation("", x1, yh, 0.0);
        leftArrow.setTipRadius(0.0);
        addAnnotation(leftArrow);
        final XYPointerAnnotation rightArrow = new XYPointerAnnotation("", x3, yh, Math.PI);
        rightArrow.setTipRadius(0.0);
        addAnnotation(rightArrow);
        // Lower D text annotation
        final String lowerDStr = String.format(test.getMeasureSymbol()
                + "=%.4f", test.getLowerMeasure());
        final XYTextAnnotation lowerDAnn = new XYTextAnnotation(lowerDStr, x1, y1);
        lowerDAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(lowerDAnn);
        // Cohen's D text annotation
        final String cohensDStr = String.format(test.getMeasureSymbol()
                + "=%.4f", test.getMeasure());
        final XYTextAnnotation cohensDAnn = new XYTextAnnotation(cohensDStr, x2, y2);
        cohensDAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(cohensDAnn);
        // Upper D text annotation
        final String upperDStr = String.format(test.getMeasureSymbol()
                + "=%.4f", test.getUpperMeasure());
        final XYTextAnnotation upperDAnn = new XYTextAnnotation(upperDStr, x3, y3);
        upperDAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(upperDAnn);
        // CI text annotation
        final int confidence = (int) (test.getConfidence() * 100);
        final String confStr = confidence + "% confidence";
        final XYTextAnnotation confAnn = new XYTextAnnotation(confStr, (x1 + x3) * 0.5, yh);
        confAnn.setTextAnchor(TextAnchor.BOTTOM_CENTER);
        addAnnotation(confAnn);
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public final void refresh() {
        setTitle(test);
        if (test.getLowerPdf() != null) {
            updateChart();
        }
    }
}