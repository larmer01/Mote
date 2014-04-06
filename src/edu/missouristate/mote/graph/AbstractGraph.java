package edu.missouristate.mote.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import edu.missouristate.mote.Constants;
import edu.missouristate.mote.events.ChangeListener;
import edu.missouristate.mote.effectsizes.AbstractTest;

/**
 * Base class for implementing charts for statistical tests.
 */
public abstract class AbstractGraph {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /**
     * Annotation references. We must keep these in order to remove them from
     * the chart.
     */
    private final transient List<AbstractXYAnnotation> annotations;

    /** The chart. */
    private final transient JFreeChart chart;

    /** Panel holding the chart. */
    private final transient ChartPanel chartPanel;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of an AbstractGraph.
     *
     * @param test statistical test to display on this graph
     */
    public AbstractGraph(final AbstractTest test) {
        annotations = new ArrayList<>();
        // Create a default empty chart
        chart = ChartFactory.createXYLineChart("Title", "x axis", "y axis",
                new DefaultXYDataset());
        chartPanel = new ChartPanel(chart);
        // Some settings
        chart.setBackgroundPaint(Color.white);
        chart.getXYPlot().setBackgroundPaint(Color.white);
        chart.getXYPlot().setDomainGridlinePaint(Constants.GRAPH_GRID_COLOR);
        chart.getXYPlot().setRangeGridlinePaint(Constants.GRAPH_GRID_COLOR);
        chart.getTitle().setFont(Constants.CHART_FONT);
        chart.getXYPlot().getDomainAxis().setLabelFont(Constants.CHART_FONT);
        chart.getXYPlot().getRangeAxis().setLabelFont(Constants.CHART_FONT);
        chart.removeLegend();
        // Listen to change events on the underlying object
        test.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged() {
                refresh();
            }
        });
    }

    // *************************************************************************
    // PROTECTED STATIC METHODS
    // *************************************************************************

    /**
     * Return the closest discrete Y value in the dataset for the specified X
     * value.
     *
     * @param xValue X value
     * @param xydata array of X/Y data
     * @return closest Y value corresponding to X value
     */
    protected static double findApproxYValue(final double xValue,
            final double[][] xydata) {
        // Lower than the smallest x value
        if (xValue < xydata[0][0]) {
            return xydata[1][0];
        // Higher than the largest x value
        } else if (xValue > xydata[0][xydata[0].length - 1]) {
            return xydata[1][xydata[0].length - 1];
        }
        // Somewhere in between
        for (int index = 1; index < xydata[0].length; index++) {
            final double lower = xydata[0][index - 1];
            final double upper = xydata[0][index];
            if (lower <= xValue && xValue <= upper) {
                return xydata[1][index];
            }
        }
        // Something went wrong
        return 0.0;
    }

    /**
     * Return the maximum Y value in the specified dataset.
     *
     * @param xydata array of X/Y data
     * @return maximum Y value
     */
    protected static double findMaxYValue(final double[][] xydata) {
        double result = xydata[1][0];
        for (int index = 1; index < xydata[1].length; index++) {
            if (xydata[1][index] > result) {
                result = xydata[1][index];
            }
        }
        return result;
    }

    // *************************************************************************
    // PROTECTED METHODS
    // *************************************************************************
    /**
     * Add an annotation to the chart.
     *
     * @param annotation annotation to add
     */
    protected final void addAnnotation(final AbstractXYAnnotation annotation) {
        annotations.add(annotation);
        chart.getXYPlot().addAnnotation(annotation);
    }

    /**
     * Remove all annotations from the chart.
     */
    protected final void removeAnnotations() {
        for (AbstractXYAnnotation ann : annotations) {
            chart.getXYPlot().removeAnnotation(ann);
        }
        annotations.clear();
    }

    /**
     * Set the chart's dataset to the specified dataset.
     *
     * @param dataset dataset
     */
    protected final void setDataset(final DefaultXYDataset dataset) {
        chart.getXYPlot().setDataset(dataset);
    }

    /**
     * Set the title of the chart based on the specified statistical test.
     *
     * @param test statistical test
     */
    protected final void setTitle(final AbstractTest test) {
        // Title line 1
        final int confidence = (int) (test.getConfidence() * 100);
        final String title1 = confidence + "% confidence";
        // Title line 2
        final String measure1 = String.format("%.4f", test.getLowerMeasure());
        final String measure2 = String.format("%.4f", test.getMeasure());
        final String measure3 = String.format("%.4f", test.getUpperMeasure());
        final String title2 = measure1 + " < " + test.getMeasureSymbol() + "="
                + measure2 + " < " + measure3;
        // Title line 3 (optional)
        String title;
        if (test.getErrorMessage().isEmpty()) {
            title = title1 + "\n" + title2;
        } else {
            title = title1 + "\n" + title2 + "\n(Warning: "
                    + test.getErrorMessage() + ")";
        }
        // Update
        chart.setTitle(new TextTitle(title, Constants.CHART_FONT));
    }

    /**
     * Set the X axis label.
     *
     * @param label label text
     */
    protected final void setXAxisLabel(final String label) {
        chart.getXYPlot().getDomainAxis().setLabel(label);
    }

    /**
     * Set the Y axis label.
     *
     * @param label label text
     */
    protected final void setYAxisLabel(final String label) {
        chart.getXYPlot().getRangeAxis().setLabel(label);
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Return the underlying chart object.
     *
     * @return chart object
     */
    public final JFreeChart getChart() {
        return chart;
    }

    /**
     * Return the underlying chart panel.
     *
     * @return chart panel object
     */
    public final ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Refresh this AbstractGraph.
     */
    public abstract void refresh();
}
