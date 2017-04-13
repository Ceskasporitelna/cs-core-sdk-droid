package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.webapi.HasValue;

/**
 * The enum Signing state.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public enum SigningState implements HasValue {

    /**
     * Done signing state.
     */
    DONE("DONE"),

    /**
     * Canceled signing state.
     */
    CANCELED("CANCELED"),

    /**
     * Open signing state.
     */
    OPEN("OPEN"),

    /**
     * None signing state.
     */
    NONE("NONE"),

    /**
     * Other signing state.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Signing state.
     *
     * @param value the value
     */
    SigningState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    /**
     * Other signing state.
     *
     * @param value the value
     * @return the signing state
     */
    public static SigningState other(String value) {
        SigningState state = SigningState.OTHER;
        state.setValue(value);
        return state;
    }
}
