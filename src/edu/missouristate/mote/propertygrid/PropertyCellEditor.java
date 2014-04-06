package edu.missouristate.mote.propertygrid;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import edu.missouristate.mote.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Editor for cells in a PropertyTable.
 */
public final class PropertyCellEditor extends AbstractCellEditor
        implements TableCellEditor {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Currently selected row. */
    private transient int currentRow;

    /** Selected object backing the table model. */
    private final transient Object selectedObject;

    /** Table row data. */
    private final transient List<PropertyTableRow> tableRows;

    /** Text field backing this cell. */
    private final transient JFormattedTextField textField;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a PropertyCellEditor.
     *
     * @param object selected object backing the model
     * @param rows data for each row in the table
     */
    public PropertyCellEditor(final Object object,
            final List<PropertyTableRow> rows) {
        super();
        // Initialize our fields
        currentRow = 0;
        selectedObject = object;
        tableRows = rows;
        textField = new JFormattedTextField();
        textField.setBorder(new EmptyBorder(Constants.CELL_INSETS));
        // This handles getting us out of editing using tab or enter if we
        // used the mouse to double click and edit the cell
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        cancelCellEditing();
                        break;
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_TAB:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                        stopCellEditing();
                    default:
                        break;
                }
            }
        });
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Update the value of the backing text field and underlying object.
     *
     * @param oldValue original value in the field
     * @param newValue new value for the field
     * @param getter underlying object's getter method
     * @param setter underlying object's setter method
     */
    private void updateValue(final double oldValue, final double newValue,
            final Method getter, final Method setter) {
        // Numbers are the same, don't update
        if (Math.abs(newValue - oldValue) < Constants.PRECISION) {
            return;
        }
        try {
            // Update the underlying object
            setter.invoke(selectedObject, newValue);
            // It's possible the object has some restrictions on values and
            // rejected our change (or altered it); retrieve the current value
            // from the underlying object
            final Double value = (Double) getter.invoke(selectedObject);
            textField.setText(value.toString());
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(PropertyCellEditor.class.getName()).log(Level.INFO,
                    null, ex);
        }
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Return the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }

    /**
     * Set an initial value for the editor. This will cause the editor to
     * stopEditing() and lose any partially edited value if the editor is
     * editing when this method is called. Return the component that should be
     * added to the client's Component hierarchy. Once installed in the client's
     * hierarchy this component will then be able to draw and receive user
     * input.
     *
     * @param table the JTable that is asking the editor to edit; can be null
     * @param value the value of the cell to be edited; it is up to the specific
     * editor to interpret and draw the value. For example, if value is the
     * string "true", it could be rendered as a string or it could be rendered
     * as a check box that is checked. null is a valid value
     * @param isSelected true if the cell is to be rendered with highlighting
     * @param row the row of the cell being edited
     * @param col the column of the cell being edited
     * @return the component for editing
     */
    @Override
    public Component getTableCellEditorComponent(final JTable table,
            final Object value, final boolean isSelected, final int row,
            final int col) {
        currentRow = row;
        textField.setText(value.toString());
        textField.selectAll();
        return textField;
    }

    /**
     * Call fireEditingStopped() and return true.
     *
     * @return true
     */
    @Override
    public boolean stopCellEditing() {
        // Make sure we have a getter and a setter
        final Method getter = tableRows.get(currentRow).getGetter();
        final Method setter = tableRows.get(currentRow).getSetter();
        if (getter == null || setter == null) {
            return super.stopCellEditing();
        }
        // Get the current (pre-editing) value from the object
        final Double oldValue;
        try {
            oldValue = (Double) getter.invoke(selectedObject);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(PropertyCellEditor.class.getName()).log(Level.INFO,
                    null, ex);
            return super.stopCellEditing();
        }
        // Get the new (edited) value from the text field
        boolean useOldValue = false;
        double newValue;
        try {
            newValue = Double.parseDouble(textField.getText());
        } catch (NumberFormatException ex) {
            newValue = 0.0;
            useOldValue = true;
        }
        // Update
        if (useOldValue) {
            textField.setText(oldValue.toString());
        } else {
            updateValue(oldValue, newValue, getter, setter);
        }
        return super.stopCellEditing();
    }
}