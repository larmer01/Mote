package edu.missouristate.mote.events;

/**
 * Provide methods for allowing clients to subscribe to changes notifications.
 */
public interface ChangeNotifier {
    /**
     * Add a new ChangeListener that should be notified on state changes.
     *
     * @param listener ChangeListener to add
     */
    void addChangeListener(final ChangeListener listener);

    /**
     * Remove an existing ChangeListener. If the specified listener has not been
     * added via addChangeListener(), no action is taken.
     *
     * @param listener ChangeListener to remove
     */
    void removeChangeListener(final ChangeListener listener);
}
