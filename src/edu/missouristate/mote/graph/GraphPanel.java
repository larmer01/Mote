package edu.missouristate.mote.graph;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import edu.missouristate.mote.effectsizes.AbstractNonCentralTest;
import edu.missouristate.mote.effectsizes.AbstractNormalTest;

/**
 * Panel encapsulating an AbstractGraph.
 */
public final class GraphPanel extends JPanel {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Graph object. */
    private transient AbstractGraph currentGraph;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Refresh the underlying graph.
     */
    public void refresh() {
        currentGraph.refresh();
    }

    /**
     * Update the underlying graph to show the specified statistical test.
     *
     * @param test statistical test
     */
    public void update(final AbstractNonCentralTest test) {
        currentGraph = new NonCentralGraph(test);
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(currentGraph.getChartPanel(), BorderLayout.CENTER);
        this.validate();
    }

    /**
     * Update the underlying graph to show the specified statistical test.
     *
     * @param test statistical test
     */
    public void update(final AbstractNormalTest test) {
        currentGraph = new NormalGraph(test);
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(currentGraph.getChartPanel(), BorderLayout.CENTER);
        this.validate();
    }
}
