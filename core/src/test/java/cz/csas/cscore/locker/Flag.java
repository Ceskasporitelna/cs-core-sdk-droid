package cz.csas.cscore.locker;

import cz.csas.cscore.webapi.HasValue;

/**
 * The enum Flag.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 18 /04/16.
 */
public enum Flag implements HasValue {

    /**
     * Direction flag.
     */
    DIRECTION("DIRECTION"),

    /**
     * Other flag.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Flag.
     *
     * @param value the value
     */
    Flag(String value) {
        this.value = value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * Other flag.
     *
     * @param value the value
     * @return the flag
     */
    public static Flag other(String value) {
        Flag flag = Flag.OTHER;
        flag.setValue(value);
        return flag;
    }
}