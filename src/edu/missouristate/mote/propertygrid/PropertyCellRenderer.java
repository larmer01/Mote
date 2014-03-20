package edu.missouristate.mote.propertygrid;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import edu.missouristate.mote.Constants;

/**
 * Renderer for cells in a PropertyTable.
 */
public class PropertyCellRenderer extends DefaultTableCellRenderer {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    private transient final List<PropertyTableRow> tableRows;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a PropertyCellRenderer.
     *
     * @param tableRows data for each row in the table
     */
    public PropertyCellRenderer(final List<PropertyTableRow> tableRows) {
        super();
        this.tableRows = tableRows;
    }

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
        // Create a formatted number for the numeric cells
        Object newValue;
        if (column == 1 && !tableRow.isCategory()) {
            try {
                final double newDouble = Double.parseDouble(value.toString());
                final DecimalFormat format = new DecimalFormat(
                        Constants.DECIMAL_FORMAT);
                newValue = format.format(newDouble);
            } catch (ClassCastException e) {
                newValue = value;
            } catch (NumberFormatException e) {
                newValue = value;
            }
        } else {
            newValue = value;
        }
        // Retrieve the cell from our super class
        final Component cell = super.getTableCellRendererComponent(table,
                newValue, isSelected, hasFocus, row, column);
        // Category rows
        if (tableRow.isCategory()) {
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
            cell.setBackground(Constants.CATEGORY_BG_COLOR);
            cell.setForeground(Constants.CATEGORY_FG_COLOR);
        } // Name column
        else if (column == 0) {
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
            cell.setBackground(Constants.NAME_BG_COLOR);
            cell.setForeground(Constants.NAME_FG_COLOR);
        } // Selected cell
        else if (isSelected) {
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
            cell.setBackground(Constants.SELECT_BG_COLOR);
            cell.setForeground(Constants.SELECT_FG_COLOR);
        } // Value column
        else {
            cell.setBackground(Constants.VALUE_BG_COLOR);
            cell.setForeground(Constants.VALUE_FG_COLOR);
        }
        return cell;
    }
}