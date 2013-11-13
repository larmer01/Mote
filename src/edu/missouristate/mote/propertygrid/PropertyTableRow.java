package edu.missouristate.mote.propertygrid;

import java.lang.reflect.Method;

/**
 * Encapsulate a row in a PropertyGrid/PropertyTable and contains the references
 * to the underlying object holding the values for the row.
 */
public class PropertyTableRow {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    // Category name
    private String category;
    // Description of the property as it will appear in the help area
    private String description;
    // Getter method for the property value
    private Method getter;
    // Name of the property as it will appear in column 0 of the grid
    private String name;
    // True if the property is read-only; false, otherwise
    private boolean readonly;
    // Setter method for the property value
    private Method setter;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a PropertyGridRow.
     */
    public PropertyTableRow() {
        category = "(no category)";
        description = "";
        name = "";
        readonly = false;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return the category name.
     *
     * @return category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category name.
     *
     * @param value category name
     */
    public void setCategory(final String value) {
        category = value;
    }

    /**
     * Return the description of the property as it will appear in the help
     * area.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the property as it will appear in the help area.
     *
     * @param value description
     */
    public void setDescription(final String value) {
        description = value;
    }

    /**
     * Return the getter method for the property value.
     *
     * @return method
     */
    public Method getGetter() {
        return getter;
    }

    /**
     * Set the getter method for the property value.
     *
     * @param value method
     */
    public void setGetter(final Method value) {
        getter = value;
    }

    /**
     * Return the name of the property as it will appear in column 0 of the
     * grid.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the property as it will appear in column 0 of the grid.
     *
     * @param value name
     */
    public void setName(final String value) {
        name = value;
    }

    /**
     * Return true if the property is read-only; false, otherwise.
     *
     * @return true if the property is read-only; false, otherwise
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Set whether the property is read-only.
     *
     * @param value true if the property is read-only; false, otherwise
     */
    public void setReadonly(final boolean value) {
        readonly = value;
    }

    /**
     * Return the setter method for the property value.
     *
     * @return method
     */
    public Method getSetter() {
        return setter;
    }

    /**
     * Set the setter method for the property value.
     *
     * @param value method
     */
    public void setSetter(final Method value) {
        setter = value;
    }

    /**
     * Return true if this row is a category row; false, otherwise.
     *
     * @return true if this row is a category row; false, otherwise
     */
    public boolean isCategory() {
        if (name == null) {
            return false;
        }
        return name.equals(category);
    }
}