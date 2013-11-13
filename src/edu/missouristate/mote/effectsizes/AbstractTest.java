package edu.missouristate.mote.effectsizes;

import java.util.ArrayList;
import java.util.List;
import edu.missouristate.mote.events.ChangeListener;
import edu.missouristate.mote.events.ChangeNotifier;

/**
 * Base class for all statistical tests.
 */
public abstract class AbstractTest implements ChangeNotifier {

    // *************************************************************************
    // FIELDS
    // *************************************************************************
    // Error message
    private String errorMessage;
    // List of listeners to notify on state changes
    private transient final List<ChangeListener> listeners;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of an AbstractTest.
     */
    public AbstractTest() {
        errorMessage = "";
        listeners = new ArrayList<ChangeListener>();
    }

    // *************************************************************************
    // PROTECTED STATIC METHODS
    // *************************************************************************
    /**
     * Return the degrees of freedom given a sample size.
     *
     * @param size sample size
     * @return degrees of freedom
     */
    protected static double calcDF(final double size) {
        return size - 1;
    }

    /**
     * Return the sample size given degrees of freedom.
     *
     * @param df degrees of freedom
     * @return sample size
     */
    protected static double calcSize(final double df) {
        return df + 1;
    }

    /**
     * Return the standard deviation given sample size and standard error.
     *
     * @param size sample size
     * @param stdErr standard error
     * @return standard deviation
     */
    protected static double calcStdDev(final double size, final double stdErr) {
        return stdErr * Math.sqrt(size);
    }

    /**
     * Return the standard error given sample size and standard deviation.
     *
     * @param size sample size
     * @param stdDev standard deviation
     * @return standard error
     */
    protected static double calcStdErr(final double size, final double stdDev) {
        if (size <= 0) {
            return 0.0;
        } else {
            return stdDev / Math.sqrt(size);
        }
    }

    // *************************************************************************
    // PROTECTED METHODS
    // *************************************************************************
    /**
     * Notify all listeners that the state of this object has changed.
     */
    protected void doStateChanged() {
        for (ChangeListener listener : listeners) {
            listener.stateChanged();
        }
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Add a new ChangeListener that should be notified on state changes.
     *
     * @param listener ChangeListener to add
     */
    @Override
    public void addChangeListener(final ChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Return the value of alpha.
     *
     * @return alpha
     */
    public abstract double getAlpha();

    /**
     * Return the value of the confidence probability.
     *
     * @return confidence probability
     */
    public abstract double getConfidence();

    /**
     * Return the current error message. If there is no error present, return
     * the empty string.
     *
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Return the value of the measure being calculated.
     *
     * @return measure value
     */
    public abstract double getMeasure();

    /**
     * Return the lower confidence interval value of the measure being
     * calculated.
     *
     * @return lower confidence interval measure value
     */
    public abstract double getLowerMeasure();

    /**
     * Return the full name of the measure.
     *
     * @return full name of the measure
     */
    public abstract String getMeasureName();

    /**
     * Return the symbol or variable representing the measure.
     *
     * @return symbol or variable representing the measure
     */
    public abstract String getMeasureSymbol();

    /**
     * Return the name of the underlying test.
     *
     * @return name of the underlying test
     */
    public abstract String getTestName();

    /**
     * Return the value of the test statistic.
     *
     * @return value of the test statistic
     */
    public abstract double getTestStatistic();

    /**
     * Return the symbol or variable representing the test statistic.
     *
     * @return value of the test statistic
     */
    public abstract String getTestStatisticSymbol();

    /**
     * Return the upper confidence interval value of the measure being
     * calculated.
     *
     * @return upper confidence interval measure value
     */
    public abstract double getUpperMeasure();

    /**
     * Remove an existing ChangeListener. If the specified listener has not been
     * added via addChangeListener(), no action is taken.
     *
     * @param listener ChangeListener to remove
     */
    @Override
    public void removeChangeListener(final ChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * Reset all parameters to their default values.
     */
    public abstract void reset();

    /**
     * Set the current error message.
     *
     * @param message error message
     */
    public void setErrorMessage(final String message) {
        errorMessage = message;
    }
}