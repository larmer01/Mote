package edu.missouristate.mote.propertygrid;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JFormattedTextField;
import javax.swing.border.EmptyBorder;
import edu.missouristate.mote.Constants;

/**
 * Renderer for cells in a PropertyTable.
 */
public final class PropertyCellRenderer extends DefaultTableCellRenderer {

    // *************************************************************************
    // FIELDS
    // *************************************************************************

    /** Table row data. */
    private final transient List<PropertyTableRow> tableRows;

    /** Text field backing this cell. */
    private final transient JFormattedTextField textField;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************

    /**
     * Initialize a new instance of a PropertyCellRenderer.
     *
     * @param rows data for each row in the table
     */
    public PropertyCellRenderer(final List<PropertyTableRow> rows) {
        super();
        tableRows = rows;
        textField = new JFormattedTextField();
        textField.setBorder(new EmptyBorder(Constants.CELL_INSETS));
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Return a formatted "value" given the cell's position in the grid.
     *
     * @param value cell value
     * @param column column number
     * @param isCategory true if the row is a category row
     * @return formatted value
     */
    private String formatValue(final Object value, final int column,
            final boolean isCategory) {
        String result;
        if (value == null) {
            result = "";
        } else if (isCategory || column != 1) {
            result = value.toString();
        } else {
            try {
                final double newDouble = Double.parseDouble(value.toString());
                final DecimalFormat format = new DecimalFormat(
                        Constants.DECIMAL_FORMAT);
                result = format.format(newDouble);
            } catch (ClassCastException | NumberFormatException e) {
                result = value.toString();
            }
        }
        return result;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Return the default table cell renderer.
     *
     * @param table the JTable
     * @param value the value to assign to the cell at [row, column]
     * @param isSelected true if cell is selected
     * @param hasFocus true if cell has focus
     * @param row the row of the cell to render
     * @param column the column of the cell to render
     * @return the default table cell renderer
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table,
            final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        final PropertyTableRow tableRow = tableRows.get(row);
        textField.setText(formatValue(value, column, tableRow.isCategory()));

        if (tableRow.isCategory()) {
            // Category rows
            textField.setFont(textField.getFont().deriveFont(Font.BOLD));
            textField.setBackground(Constants.CATEGORY_BG_COLOR);
            textField.setForeground(Constants.CATEGORY_FG_COLOR);
        } else if (column == 0) {
            // Name column
            textField.setFont(textField.getFont().deriveFont(Font.BOLD));
            textField.setBackground(Constants.NAME_BG_COLOR);
            textField.setForeground(Constants.NAME_FG_COLOR);
        } else if (isSelected) {
            // Selected cell
            textField.setFont(textField.getFont().deriveFont(Font.BOLD));
            textField.setBackground(Constants.SELECT_BG_COLOR);
            textField.setForeground(Constants.SELECT_FG_COLOR);
        } else {
            // Value column
            textField.setFont(textField.getFont().deriveFont(Font.PLAIN));
            textField.setBackground(Constants.VALUE_BG_COLOR);
            textField.setForeground(Constants.VALUE_FG_COLOR);
        }
        return textField;
    }
}