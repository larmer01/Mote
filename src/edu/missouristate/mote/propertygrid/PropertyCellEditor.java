package edu.missouristate.mote.propertygrid;

import java.awt.Component;
import java.awt.Insets;
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
import edu.missouristate.mote.Utilities;

/**
 * Editor for cells in a PropertyTable.
 */
public class PropertyCellEditor extends AbstractCellEditor
        implements TableCellEditor {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    private transient int currentRow;
    private transient final Object selectedObject;
    private transient final List<PropertyTableRow> tableRows;
    private transient final JFormattedTextField textField;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a PropertyCellEditor.
     *
     * @param selectedObject selected object backing the model
     * @param tableRows data for each row in the table
     */
    public PropertyCellEditor(final Object selectedObject,
            final List<PropertyTableRow> tableRows) {
        super();
        // Initialize our fields
        currentRow = 0;
        this.selectedObject = selectedObject;
        this.tableRows = tableRows;
        textField = new JFormattedTextField();
        // The default border makes it almost impossible to edit
        textField.setBorder(new EmptyBorder(new Insets(1, 0, 1, 0)));
        // This handles getting us out of editing using tab or enter if we
        // used the mouse to double click and edit the cell
        textField.addKeyListener(new KeyAdapter() {
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
                }
            }
        });
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
    
    public int getRow() {
        return currentRow;
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
        // Get the new value
        double newValue;
        boolean newIsValid = true;
        try {
            newValue = Double.parseDouble(textField.getText());
        } catch (ClassCastException e) {
            newValue = 0.0;
            newIsValid = false;
        } catch (NumberFormatException e) {
            newValue = 0.0;
            newIsValid = false;
        }
        try {
            // Get the old value
            final double oldValue = (Double) getter.invoke(selectedObject);
            // Update the underlying object if the values are different and if
            // the new value is a valid number
            if (newIsValid && !Utilities.areEqual(oldValue, newValue)) {
                setter.invoke(selectedObject, newValue);
            }
            // It's possible the object has some restrictions on values and
            // rejected our change (or altered it); retrieve the current
            // value from the underlying object
            final Double value = (Double) getter.invoke(selectedObject);
            textField.setText(value.toString());
        } catch (IllegalAccessException ex) {
        } catch (InvocationTargetException ex) {
        }
        return super.stopCellEditing();
    }
}