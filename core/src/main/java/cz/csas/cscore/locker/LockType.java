package cz.csas.cscore.locker;

/**
 * The enum Lock type. You can choose whichever you want from PIN,GESTURE and NONE LockType.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public enum LockType {

    /**
     * Pin lock type.
     */
    PIN,

    /**
     * Gesture lock type.
     */
    GESTURE,

    /**
     * Fingerprint lock type.
     */
    FINGERPRINT,

    /**
     * None lock type.
     */
    NONE;
}
