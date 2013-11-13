package edu.missouristate.mote.propertygrid;

import java.awt.Color;
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

/**
 *
 * @author tim
 */
public class PropertyGrid extends JPanel {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    // TextArea holding the help text
    private JTextArea help = new JTextArea();
    // ScrollPanel holding the help TextArea
    private JScrollPane helpScrollPane = new JScrollPane();
    // Table holding the properties
    private PropertyTable table = new PropertyTable();
    // ScrollPane holding the properties Table
    private final JScrollPane tableScrollPane = new JScrollPane();

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a PropertiesGrid.
     */
    public PropertyGrid() {
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
        help.setBackground(new Color(238, 238, 238));
        help.setColumns(20);
        help.setLineWrap(true);
        help.setRows(5);
        help.setWrapStyleWord(true);
        help.setAutoscrolls(false);
        help.setBorder(BorderFactory.createLineBorder(new Color(142, 142, 142), 0));
        help.setDragEnabled(false);
        help.setFocusTraversalKeysEnabled(false);
        helpScrollPane.setViewportView(help);
    }

    /**
     * Initialize the panel layout.
     */
    private void initLayout() {
        setBackground(new java.awt.Color(255, 255, 255));
        final GroupLayout mainLayout = new GroupLayout(this);
        this.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.LEADING)
                .add(tableScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .add(helpScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.LEADING)
                .add(mainLayout.createSequentialGroup()
                .add(tableScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(helpScrollPane, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)));
    }

    /**
     * Initialize the properties table.
     */
    private void initTable() {
        tableScrollPane.setViewportView(table);
        // Make sure we're notifyed when the selection changes
        final ListSelectionModel rowSelMod = table.getSelectionModel();
        rowSelMod.addListSelectionListener(new PropertyGrid.SelectionListener());
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
        public void valueChanged(final ListSelectionEvent le) {
            help.setText(table.getDescription());
        }
    }
}