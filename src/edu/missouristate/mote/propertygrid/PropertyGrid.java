package edu.missouristate.mote.propertygrid;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import edu.missouristate.mote.events.ChangeNotifier;
import edu.missouristate.mote.Constants;

/**
 * A limited .NET-like property grid for numerical data.
 */
public final class PropertyGrid extends JPanel {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** TextArea holding the help text. */
    private final transient JTextArea help;

    /** ScrollPanel holding the help TextArea. */
    private final transient JScrollPane helpScrollPane;

    /** Table holding the properties. */
    private final transient PropertyTable table;

    /** ScrollPane holding the properties Table. */
    private final transient JScrollPane tableScrollPane;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a PropertiesGrid.
     */
    public PropertyGrid() {
        super();
        help = new JTextArea();
        helpScrollPane = new JScrollPane();
        table = new PropertyTable();
        tableScrollPane = new JScrollPane();
        initTable();
        initHelp();
        initLayout();
        this.tableScrollPane.getViewport().setBackground(this.getBackground());
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Initialize the help area.
     */
    private void initHelp() {
        help.setEditable(false);
        help.setBackground(Constants.NAME_BG_COLOR);
        help.setLineWrap(true);
        help.setWrapStyleWord(true);
        help.setAutoscrolls(false);
        help.setBorder(BorderFactory.createLineBorder(
                Constants.GRAPH_GRID_COLOR, 0));
        help.setDragEnabled(false);
        help.setFocusTraversalKeysEnabled(false);
        helpScrollPane.setViewportView(help);
    }

    /**
     * Initialize the panel layout.
     */
    private void initLayout() {
        setBackground(Constants.FORM_BG_COLOR);
        final GroupLayout mainLayout = new GroupLayout(this);
        this.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.LEADING)
                .add(tableScrollPane, GroupLayout.PREFERRED_SIZE, 0,
                        Short.MAX_VALUE)
                .add(helpScrollPane, GroupLayout.DEFAULT_SIZE,
                        Constants.GRID_MIN_WIDTH, Short.MAX_VALUE));
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.LEADING)
                .add(mainLayout.createSequentialGroup()
                .add(tableScrollPane, GroupLayout.DEFAULT_SIZE,
                        Constants.GRID_MIN_WIDTH, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(helpScrollPane, GroupLayout.PREFERRED_SIZE,
                        Constants.HELP_MIN_HEIGHT,
                        GroupLayout.PREFERRED_SIZE)));
    }

    /**
     * Initialize the properties table.
     */
    private void initTable() {
        tableScrollPane.setViewportView(table);
        // Make sure we're notifyed when the selection changes
        final ListSelectionModel rowSelMod = table.getSelectionModel();
        rowSelMod.addListSelectionListener(
                new PropertyGrid.SelectionListener());
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return the object backing the table model.
     *
     * @return object
     */
    public Object getSelectedObject() {
        return table.getSelectedObject();
    }

    /**
     * Refresh the model from the underlying object.
     */
    public void refresh() {
        table.refresh();
    }

    /**
     * Set the object backing the table model.
     *
     * @param object object
     */
    public void setSelectedObject(final ChangeNotifier object) {
        table.setSelectedObject(object);
    }

    // *************************************************************************
    // INNER CLASSES
    // *************************************************************************

    /**
     * Handle changes to the selection.
     */
    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent event) {
            help.setText(table.getDescription());
        }
    }
}