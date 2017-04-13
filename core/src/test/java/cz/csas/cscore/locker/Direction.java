package cz.csas.cscore.locker;

import cz.csas.cscore.webapi.HasValue;

/**
 * The enum Direction.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 18 /04/16.
 */
public enum Direction implements HasValue {

    /**
     * Prague direction.
     */
    PRAGUE("PRAGUE"),

    /**
     * Brno direction.
     */
    BRNO("BRNO"),

    /**
     * Other direction.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Direction.
     *
     * @param value the value
     */
    Direction(String value) {
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
     * Other direction.
     *
     * @param value the value
     * @return the direction
     */
    public static Direction other(String value) {
        Direction direction = Direction.OTHER;
        direction.setValue(value);
        return direction;
    }
}