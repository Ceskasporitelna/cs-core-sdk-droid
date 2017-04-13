package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.webapi.HasValue;

/**
 * The enum Authorization type summarizes the types of signing authorization.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public enum AuthorizationType implements HasValue {

    /**
     * Tac authorization type.
     */
    TAC("TAC"),

    /**
     * Mobile case authorization type.
     */
    MOBILE_CASE("CASE_MOBILE"),

    /**
     * None authorization type.
     */
    NONE("NONE"),

    /**
     * Other authorization type.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Authorization type.
     *
     * @param value the value
     */
    AuthorizationType(String value) {
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
     * Other authorization type.
     *
     * @param value the value
     * @return the authorization type
     */
    public static AuthorizationType other(String value) {
        AuthorizationType type = AuthorizationType.OTHER;
        type.setValue(value);
        return type;
    }
}
