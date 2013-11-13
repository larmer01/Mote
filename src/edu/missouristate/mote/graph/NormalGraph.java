package edu.missouristate.mote.graph;

import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.TextAnchor;
import edu.missouristate.mote.effectsizes.AbstractNormalTest;

/**
 *
 * @author tim
 */
public class NormalGraph extends AbstractGraph {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    private transient final AbstractNormalTest test;
    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a GraphPanel.
     */
    public NormalGraph(final AbstractNormalTest test) {
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
        dataset.addSeries(0, test.getPdf());
        setDataset(dataset);
        // Confidence interval x/y values
        final double x1 = -test.getDeviations();
        final double pdfHeight = findMaxYValue(test.getPdf());
        final double pdfCurrent = findApproxYValue(x1, test.getPdf());
        final double y1 = Math.max(pdfCurrent, pdfHeight * 0.5);
        final double x2 = test.getDeviations();
        final double y2 = y1;
        final double yh = y2 * 0.8;
        // Confidence interval vertical line annotations
        removeAnnotations();
        addAnnotation(new XYLineAnnotation(x1, 0.0, x1, y1));
        addAnnotation(new XYLineAnnotation(x2, 0.0, x2, y2));
        // Confidence interval horizontal line annotation
        addAnnotation(new XYLineAnnotation(x1, yh, x2, yh));
        final XYPointerAnnotation leftArrow = new XYPointerAnnotation("", x1, yh, 0.0);
        leftArrow.setTipRadius(0.0);
        addAnnotation(leftArrow);
        final XYPointerAnnotation rightArrow = new XYPointerAnnotation("", x2, yh, Math.PI);
        rightArrow.setTipRadius(0.0);
        addAnnotation(rightArrow);
        // Lower D text annotation
        final String lowerDStr = String.format(test.getMeasureSymbol()
                + "=%.4f", test.getLowerMeasure());
        final XYTextAnnotation lowerDAnn = new XYTextAnnotation(lowerDStr, x1, y1);
        lowerDAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        addAnnotation(lowerDAnn);
        // Upper D text annotation
        final String upperDStr = String.format(test.getMeasureSymbol()
                + "=%.4f", test.getUpperMeasure());
        final XYTextAnnotation upperDAnn = new XYTextAnnotation(upperDStr, x2, y2);
        if (test.getAlpha() < 0.03) {
            upperDAnn.setTextAnchor(TextAnchor.BOTTOM_RIGHT);
        } else {
            upperDAnn.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        }
        addAnnotation(upperDAnn);
        // CI text annotation
        final int confidence = (int) ((1 - test.getAlpha()) * 100);
        final String confStr = confidence + "% confidence";
        final XYTextAnnotation confAnn = new XYTextAnnotation(confStr, (x1 + x2) * 0.5, yh);
        confAnn.setTextAnchor(TextAnchor.BOTTOM_CENTER);
        addAnnotation(confAnn);
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public final void refresh() {
        setTitle(test);
        if (test.getPdf() != null) {
            updateChart();
        }
    }
}