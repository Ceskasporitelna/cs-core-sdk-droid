package cz.csas.cscore.locker;

/**
 * The interface On locker status changed listener. Common listener for changes of LockerStatus.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 02 /12/15.
 */
public interface OnLockerStatusChangeListener {
    /**
     * On locker status changed.
     */
    void onLockerStatusChanged(State state);
}
