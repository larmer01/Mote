package edu.missouristate.mote.propertygrid;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import edu.missouristate.mote.events.ChangeListener;
import edu.missouristate.mote.events.ChangeNotifier;
import edu.missouristate.mote.Constants;

/**
 * Table component of the property grid.
 */
public final class PropertyTable extends JTable {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Selected object backing the model. */
    private ChangeNotifier selectedObject;

    /** Data for each row in the table. */
    private final transient List<PropertyTableRow> tableRows;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a PropertyTable.
     */
    public PropertyTable() {
        super();
        tableRows = new ArrayList<>();
        // Setup an empty model
        final Object[][] data = new Object[][]{{null, null}};
        this.setModel(new DefaultTableModel(data, Constants.GRID_COL_NAMES));
        // General settings
        this.setCellSelectionEnabled(true);
        this.setEditingColumn(1);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setGridColor(Constants.GRAPH_GRID_COLOR);
        this.setShowGrid(true);
        this.setRowHeight(Constants.CELL_HEIGHT);
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Return the row number for the next non-category row searching either
     * forwards or backwards through the table. If no non-category row can be
     * found, return the current row number.
     *
     * @param isForward true to search forwards; false, backwards
     * @return next non-category row number
     */
    private int getNextRow(final boolean isForward) {
        final int step = isForward ? 1 : -1;
        int counter = 0, currentRow = getSelectedRow();
        boolean isFound = false;
        while (!isFound && counter < tableRows.size() + 1) {
            currentRow += step;
            if (currentRow >= tableRows.size()) {
                currentRow = 0;
            } else if (currentRow < 0) {
                currentRow = tableRows.size() - 1;
            }
            if (!tableRows.get(currentRow).isCategory()) {
                //&& !tableRows.get(currentRow).isReadonly()) {
                isFound = true;
            }
            counter++;
        }
        return isFound ? currentRow : getSelectedRow();
    }

    /**
     * Resize the columns in this table.
     */
    private void resizeColumns() {
        final int nameWidth = (int) (this.getWidth() * Constants.GRID_NAME_PCT);
        this.getColumnModel().getColumn(0).setPreferredWidth(nameWidth);
    }

    // *************************************************************************
    // PROTECTED METHODS
    // *************************************************************************

    /**
     * Handle key events, only allowing certain cells to be selected.
     *
     * @param e event arguments
     */
    @Override
    protected void processKeyEvent(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (e.getID() != KeyEvent.KEY_PRESSED) {
        // Let anything other than key presses pass through
            super.processKeyEvent(e);
        } else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
            // Ignore the left/right arrow keys
            e.consume();
        } else if (code == KeyEvent.VK_UP
                || (code == KeyEvent.VK_ENTER && e.isShiftDown())
                || (code == KeyEvent.VK_TAB && e.isShiftDown())) {
            // Move backward
            final int newRow = getNextRow(false);
            super.processKeyEvent(e);
            this.changeSelection(newRow, 1, false, false);
        }  else if (code == KeyEvent.VK_DOWN
                || (code == KeyEvent.VK_ENTER && !e.isShiftDown())
                || (code == KeyEvent.VK_TAB && !e.isShiftDown())) {
            // Move forward
            final int newRow = getNextRow(true);
            super.processKeyEvent(e);
            this.changeSelection(newRow, 1, false, false);
        } else {
            // Normal processing
            super.processKeyEvent(e);
        }
    }

    /**
     * Handle mouse events, only allowing certain cells to be selected.
     *
     * @param e event arguments
     */
    @Override
    protected void processMouseEvent(final MouseEvent e) {
        final int row = this.rowAtPoint(e.getPoint());
        final int col = this.columnAtPoint(e.getPoint());
        boolean ignore = false;
        if (e.getClickCount() == 0) {
            // Only care about clicks
            ignore = true;
        } else if (row < 0 || row >= tableRows.size() || col < 0 || col > 1) {
            // Invalid rows/cols
            ignore = true;
        } else if (col == 0 || tableRows.get(row).isCategory()) {
            // Can't click on property name/category cells
            ignore = true;
        }
        if (ignore) {
            e.consume();
        } else {
            super.processMouseEvent(e);
        }
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Return the description for the current selection.
     *
     * @return current row description
     */
    public String getDescription() {
        final int row = this.getSelectedRow();
        if (row > -1 && row < tableRows.size()) {
            return tableRows.get(row).getDescription();
        }
        return "";
    }

    /**
     * Return the object backing the table model.
     *
     * @return selected object
     */
    public Object getSelectedObject() {
        return selectedObject;
    }

    /**
     * Return false for any cell in the name column or category row; otherwise
     * the default value for the editable property of the cell.
     *
     * @param row row number
     * @param col column number
     * @return True if the cell is editable; false, otherwise
     */
    @Override
    public boolean isCellEditable(final int row, final int col) {
        // Invalid row
        if (row < 0 || row >= tableRows.size()) {
            return false;
        }
        // Property/category name
        if (col == 0) {
            return false;
        }
        // Category row
        if (tableRows.get(row).isCategory()) {
            return false;
        }
        // Read-only column
        if (tableRows.get(row).isReadonly()) {
            return false;
        }
        return super.isCellEditable(row, col);
    }

    /**
     * Refresh the model from the underlying object.
     */
    public void refresh() {
        for (int row = 0; row < tableRows.size(); row++) {
            final Method getter = tableRows.get(row).getGetter();
            try {
                if (getter != null) {
                    final Double value = (Double) getter.invoke(selectedObject);
                    this.setValueAt(value.toString(), row, 1);
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                this.setValueAt("", row, 1);
            }
        }
    }

    /**
     * Set the object backing the table model.
     *
     * @param object object
     */
    public void setSelectedObject(final ChangeNotifier object) {
        // Set object and rows
        selectedObject = object;
        tableRows.clear();
        tableRows.addAll(ObjectLoader.getRows(object));
        // Create a new model
        final Object[][] data = new Object[tableRows.size()][2];
        for (int index = 0; index < tableRows.size(); index++) {
            data[index][0] = tableRows.get(index).getName();
            try {
                final Method getter = tableRows.get(index).getGetter();
                if (getter != null) {
                    data[index][1] = getter.invoke(object);
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                data[index][1] = "";
            }
        }
        this.setModel(new DefaultTableModel(data, Constants.GRID_COL_NAMES));
        resizeColumns();
        // Add custom renderers for each column and an editor for the values
        final PropertyCellEditor editor = new PropertyCellEditor(selectedObject,
                tableRows);
        final PropertyCellRenderer renderer = new PropertyCellRenderer(
                tableRows);
        this.getColumnModel().getColumn(0).setCellRenderer(renderer);
        this.getColumnModel().getColumn(1).setCellRenderer(renderer);
        this.getColumnModel().getColumn(1).setCellEditor(editor);
        // Set our row to the first available one to edit
        this.changeSelection(0, 1, false, false);
        final int row = getNextRow(true);
        this.changeSelection(row, 1, false, false);
        // Listen to change events on the underlying object
        selectedObject.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged() {
                refresh();
            }
        });
    }
}