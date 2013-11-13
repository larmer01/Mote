package edu.missouristate.mote.graph;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import edu.missouristate.mote.effectsizes.AbstractNonCentralTest;
import edu.missouristate.mote.effectsizes.AbstractNormalTest;

/**
 *
 * @author tim
 */
public class GraphPanel extends JPanel {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    private transient AbstractGraph currentGraph;

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    public void refresh() {
        currentGraph.refresh();
    }

    public void update(final AbstractNonCentralTest test) {
        currentGraph = new NonCentralGraph(test);
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(currentGraph.getChartPanel(), BorderLayout.CENTER);
        this.validate();
    }

    public void update(final AbstractNormalTest test) {
        currentGraph = new NormalGraph(test);
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(currentGraph.getChartPanel(), BorderLayout.CENTER);
        this.validate();
    }
}