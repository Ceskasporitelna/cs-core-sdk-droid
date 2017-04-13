package cz.csas.cscore.webapi;

/**
 * The enum User sort parameters.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /04/16.
 */
public enum UserSortableFields implements HasValue {

    /**
     * Name user sort parameters.
     */
    NAME("name"),

    /**
     * User id user sort parameters.
     */
    USER_ID("userId"),

    /**
     * Other user sort parameters.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new User sort parameters.
     *
     * @param value the value
     */
    UserSortableFields(String value) {
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
     * Other user sort parameters.
     *
     * @param value the value
     * @return the user sort parameters
     */
    public static UserSortableFields other(String value){
        UserSortableFields parameters = UserSortableFields.OTHER;
        parameters.setValue(value);
        return parameters;
    }
}
