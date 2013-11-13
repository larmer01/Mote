package edu.missouristate.mote.events;

/**
 * Provide a method for indicating that the state of an object has changed.
 */
public interface ChangeListener {

    /**
     * Indicate that the state of an object has changed.
     */
    void stateChanged();
}