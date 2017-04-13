package cz.csas.cscore.webapi;

/**
 * The enum Gender.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 17 /03/16.
 */
public enum Gender implements HasValue {

    /**
     * Male gender.
     */
    MALE("male"),

    /**
     * Female gender.
     */
    FEMALE("female"),

    /**
     * Other gender.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Gender.
     *
     * @param value the value
     */
    Gender(String value) {
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
     * Other gender.
     *
     * @param value the value
     * @return the gender
     */
    public static Gender other(String value){
        Gender flag = Gender.OTHER;
        flag.setValue(value);
        return flag;
    }
}
