package edu.missouristate.mote;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import edu.missouristate.mote.effectsizes.*;
import edu.missouristate.mote.graph.GraphPanel;
import edu.missouristate.mote.propertygrid.PropertyGrid;

/**
 * Main application window.
 */
public final class Mote extends JFrame {

    // *************************************************************************
    // PRIVATE FIELDS
    // *************************************************************************
    private transient final PropertyGrid dataGrid;
    private transient final GraphPanel graphPanel;
    private transient AbstractTest selectedTest;

    // *************************************************************************
    // PUBLIC CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Mote form.
     */
    public Mote() {
        super();
        dataGrid = new PropertyGrid();
        graphPanel = new GraphPanel();
        // Setup the form
        setupMenu();
        setupLayout();
        // Our default test
        updateTest(new CohenDZ());
    }

    // *************************************************************************
    // PRIVATE STATIC METHODS
    // *************************************************************************
    /**
     * Return a menu item that is disabled by default. This is primary used to
     * create place holders during the development process.
     *
     * @param text menu item text
     * @return menu item
     */
    private static JMenuItem createDisabledMenuItem(final String text) {
        final JMenuItem result = new JMenuItem();
        result.setText(text);
        result.setEnabled(false);
        return result;
    }

    /**
     * Return a menu item with the specified text and associated action.
     *
     * @param text menu item text
     * @param action action to perform when clicked
     * @param accelerator accelerator key stroke
     * @return menu item
     */
    private static JMenuItem createMenuItem(final String text,
            final ActionListener action, final KeyStroke accelerator) {
        final JMenuItem result = new JMenuItem();
        if (accelerator != null) {
            result.setAccelerator(accelerator);
        }
        result.setText(text);
        result.addActionListener(action);
        return result;
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Return a menu item based on the specified test. When clicked, the action
     * will be to create and display a new instance of the test.
     *
     * @param test test to associate with the menu item
     * @return menu item
     */
    private JMenuItem createMenuItem(final AbstractNonCentralTest test) {
        final JMenuItem result = new JMenuItem();
        result.setText(test.getMeasureName() + " - " + test.getTestName());
        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                try {
                    updateTest(test.getClass().newInstance());
                } catch (InstantiationException ex) {
                    result.setEnabled(false);
                } catch (IllegalAccessException ex) {
                    result.setEnabled(false);
                }
            }
        };
        result.addActionListener(listener);
        return result;
    }

    /**
     * Return a menu item based on the specified test. When clicked, the action
     * will be to create and display a new instance of the test.
     *
     * @param test test to associate with the menu item
     * @return menu item
     */
    private JMenuItem createMenuItem(final AbstractNormalTest test) {
        final JMenuItem result = new JMenuItem();
        result.setText(test.getMeasureName() + " - " + test.getTestName());
        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                try {
                    updateTest(test.getClass().newInstance());
                } catch (InstantiationException ex) {
                    result.setEnabled(false);
                } catch (IllegalAccessException ex) {
                    result.setEnabled(false);
                }
            }
        };
        result.addActionListener(listener);
        return result;
    }

    /**
     * Return an instance of an "Edit" menu.
     *
     * @return menu
     */
    private JMenu createEditMenu() {
        final JMenu result = new JMenu();
        result.setText("Edit");
        result.add(createMenuItem("Reset Data", new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                selectedTest.reset();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)));
        return result;
    }

    /**
     * Return an instance of a "Measures" menu.
     *
     * @return menu
     */
    private JMenu createMeasuresMenu() {
        final JMenu result = new JMenu();
        result.setText("Measures");
        // t Distribution
        result.add(createMenuItem(new CohenDZ()));
        result.add(createMenuItem(new CohenDSst()));
        result.add(createMenuItem(new CohenDDepTAvgs()));
        result.add(createMenuItem(new CohenDDepTDiff()));
        result.add(createMenuItem(new CohenDIndT()));
        result.add(createMenuItem(new CohenDR()));
        result.add(createMenuItem(new HedgesGIndT()));
        result.add(createMenuItem(new GlassDIndT()));
        // F Distribution
        result.add(new Separator());
        result.add(createDisabledMenuItem("R" + Constants.SUPERSCRIPT2 + " - F Test Omnibus"));
        result.add(createDisabledMenuItem(Constants.DELTA_UPPER + "R" + Constants.SUPERSCRIPT2 + " - F Test Change"));
        result.add(createMenuItem(new Eta2FOmni()));
        result.add(createMenuItem(new PEta2FEff()));
        result.add(createMenuItem(new Omega2FOmni()));
        result.add(createMenuItem(new POmega2FEff()));
        result.add(createMenuItem(new RIntraCorrF()));
        // Chi-Squared Distribution
        result.add(new Separator());
        result.add(createDisabledMenuItem(Constants.PHI_UPPER + " - " + Constants.CHI_LOWER + Constants.SUPERSCRIPT2));
        // Odds/Risk
        result.add(new Separator());
        result.add(createMenuItem(new OddsRisk()));
        return result;
    }

    /**
     * Return an instance of a "Help" menu.
     *
     * @return menu
     */
    public JMenu createHelpMenu() {
        final JMenu result = new JMenu();
        result.setText("Help");
        result.add(createDisabledMenuItem("User's Guide"));
        result.add(createDisabledMenuItem("About"));
        return result;
    }

    /**
     * Layout the form.
     */
    private void setupLayout() {
        // Data grid
        dataGrid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Data", TitledBorder.LEFT,
                TitledBorder.TOP));
        // Graph panel
        graphPanel.setBackground(new Color(255, 255, 255));
        graphPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Confidence Interval",
                TitledBorder.LEFT, TitledBorder.TOP));
        final GroupLayout graphPanelLayout = new GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
                graphPanelLayout.createParallelGroup(GroupLayout.LEADING)
                .add(0, 630, Short.MAX_VALUE));
        graphPanelLayout.setVerticalGroup(
                graphPanelLayout.createParallelGroup(GroupLayout.LEADING)
                .add(0, 517, Short.MAX_VALUE));
        // Form
        setTitle("Mote");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(255, 255, 255));
        getContentPane().setBackground(getBackground());
        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(dataGrid, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(graphPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                .add(dataGrid, GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .add(graphPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap()));
        pack();
    }

    /**
     * Setup the form's main menu.
     */
    private void setupMenu() {
        final JMenuBar menu = new JMenuBar();
        menu.add(createEditMenu());
        menu.add(createMeasuresMenu());
        menu.add(createHelpMenu());
        setJMenuBar(menu);
    }

    /**
     * Set the selectedTest to 'test' and update the form.
     *
     * @param test test
     */
    private void updateTest(final AbstractNonCentralTest test) {
        selectedTest = test;
        dataGrid.setSelectedObject(selectedTest);
        graphPanel.update(test);
        updateTitle(test);
    }

    /**
     * Set the selectedTest to 'test' and update the form.
     *
     * @param test test
     */
    private void updateTest(final AbstractNormalTest test) {
        selectedTest = test;
        dataGrid.setSelectedObject(selectedTest);
        graphPanel.update(test);
        updateTitle(test);
    }

    /**
     * Update the form's title based on the specified test.
     *
     * @param test test
     */
    private void updateTitle(final AbstractTest test) {
        setTitle("Mote: " + test.getMeasureName() + " - " + test.getTestName());
    }

    // *************************************************************************
    // PUBLIC STATIC METHODS
    // *************************************************************************
    /**
     * Main application entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String args[]) {
        // Set the Nimbus look and feel
        try {
            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Mote.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Mote.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Mote.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Mote.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Create and display the form
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mote().setVisible(true);
            }
        });
    }
}