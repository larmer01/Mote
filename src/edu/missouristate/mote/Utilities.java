package edu.missouristate.mote;

/**
 * Common utility functions used by the entire project.
 */
public final class Utilities {

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a Utilities.
     */
    private Utilities() {
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return true if the absolute difference between the two values is less
     * than Settings.EQUAL_DELTA.
     *
     * @param value1 first value
     * @param value2 second value
     * @return true if the two values are equal within delta
     */
    public static boolean areEqual(final double value1, final double value2) {
        return areEqual(value1, value2, Constants.EQUAL_DELTA);
    }

    /**
     * Return true if the absolute difference between the two values is less
     * than the specified delta.
     *
     * @param value1 first value
     * @param value2 second value
     * @param delta delta
     * @return true if the two values are equal within delta
     */
    public static boolean areEqual(final double value1, final double value2,
            final double delta) {
        return Math.abs(value1 - value2) < delta;
    }

    /**
     * Return value rounded to Settings.DISPLAY_DIGITS digits. Positive values
     * for digits round to the right of the decimal place, negative values to
     * the left.
     *
     * @param value value to round
     * @return rounded value
     */
    public static double round(final double value) {
        return round(value, Constants.DISPLAY_DIGITS);
    }

    /**
     * Return value rounded to the specified number of digits. Positive values
     * for digits round to the right of the decimal place, negative values to
     * the left.
     *
     * @param value value to round
     * @param digits number of digits
     * @return rounded value
     */
    public static double round(final double value, final int digits) {
        final double multiplier = Math.pow(10, digits);
        return Math.round(value * multiplier) / multiplier;
    }
}